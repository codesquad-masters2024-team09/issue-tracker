package com.issuetracker.domain.member;

import com.issuetracker.domain.member.request.LoginRequest;
import com.issuetracker.domain.member.request.LogoutRequest;
import com.issuetracker.domain.member.request.SignUpRequest;
import com.issuetracker.domain.member.response.AuthResponse;
import com.issuetracker.global.exception.member.InvalidLoginDataException;
import com.issuetracker.global.exception.member.InvalidTokenException;
import com.issuetracker.global.exception.member.MemberDuplicateException;
import com.issuetracker.global.exception.member.MemberNotFoundException;
import com.issuetracker.global.security.JwtTokenProvider;
import com.issuetracker.global.security.PasswordEncoder;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public boolean idDuplicateCheck(String memberId) {
        return memberRepository.existsById(memberId);
    }

    public AuthResponse signup(SignUpRequest request) {
        if (idDuplicateCheck(request.getMemberId())) {
            throw new MemberDuplicateException();
        }

        Member member = request.toEntity(passwordEncoder.encode(request.getPassword()));

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getId(), Map.of("imgUrl", member.getProfileImgUrl()));
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), null);
        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(InvalidLoginDataException::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getEncodedPassword())) {
            throw new InvalidLoginDataException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getId(), Map.of("imgUrl", member.getProfileImgUrl()));
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), null);
        member.updateRefreshToken(refreshToken);
        memberRepository.updateRefreshToken(member.getId(), member.getRefreshToken());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(LogoutRequest request) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(MemberNotFoundException::new);
        member.expireRefreshToken();
        memberRepository.updateRefreshToken(member.getId(), member.getRefreshToken());
    }

    public AuthResponse refresh (String bearerToken) {
        String token = jwtTokenProvider.getToken(bearerToken);
        Claims claims = jwtTokenProvider.validateToken(token);
        String memberId = claims.getSubject();

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidTokenException::new);

        if (member.getRefreshToken() == null || !member.getRefreshToken().equals(token)) {
            throw new InvalidTokenException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getId(), Map.of("imgUrl", member.getProfileImgUrl()));
        String refreshToken = member.getRefreshToken();

        if(!jwtTokenProvider.refreshTokenExpired(claims)){
            refreshToken = jwtTokenProvider.createRefreshToken(memberId, null);
            member.updateRefreshToken(refreshToken);
            memberRepository.updateRefreshToken(memberId, refreshToken);
        }

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
