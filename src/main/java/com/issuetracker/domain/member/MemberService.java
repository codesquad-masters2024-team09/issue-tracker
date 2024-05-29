package com.issuetracker.domain.member;

import com.issuetracker.domain.member.request.LogInRequest;
import com.issuetracker.domain.member.request.SignUpRequest;
import com.issuetracker.domain.member.response.Auth;
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

import java.util.List;
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

    public Auth signup(SignUpRequest request) {
        if (idDuplicateCheck(request.getMemberId())) {
            throw new MemberDuplicateException();
        }

        Member member = request.toEntity(passwordEncoder.encode(request.getPassword()));
        String profileImgUrl = member.getProfileImgUrl();

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getId(), profileImgUrl != null ? Map.of("imgUrl", profileImgUrl) : null);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), null);
        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);

        return Auth.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Auth login(LogInRequest request) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(InvalidLoginDataException::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getEncodedPassword())) {
            throw new InvalidLoginDataException();
        }

        String profileImgUrl = member.getProfileImgUrl();
        String accessToken = jwtTokenProvider.createAccessToken(
                member.getId(), profileImgUrl != null ? Map.of("imgUrl", profileImgUrl) : null);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), null);
        member.updateRefreshToken(refreshToken);
        memberRepository.updateRefreshToken(member.getId(), member.getRefreshToken());

        return Auth.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String token) {
        Claims claims = jwtTokenProvider.validateToken(token);
        String memberId = claims.getSubject();
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        member.expireRefreshToken();
        memberRepository.updateRefreshToken(member.getId(), member.getRefreshToken());
    }

    public void withdraw(String token) {
        Claims claims =jwtTokenProvider.validateToken(token);
        String memberId = claims.getSubject();
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        memberRepository.deleteById(member.getId());
    }

    public Auth refresh (String token) {
        Claims claims = jwtTokenProvider.validateToken(token);
        String memberId = claims.getSubject();

        Member member = memberRepository.findById(memberId).orElseThrow(InvalidTokenException::new);

        if (member.getRefreshToken() == null || !member.getRefreshToken().equals(token)) {
            throw new InvalidTokenException();
        }

        String profileImgUrl = member.getProfileImgUrl();
        String accessToken = jwtTokenProvider.createAccessToken(
                member.getId(), profileImgUrl != null ? Map.of("imgUrl", profileImgUrl) : null);
        String refreshToken = member.getRefreshToken();

        if(!jwtTokenProvider.refreshTokenExpired(claims)){
            refreshToken = jwtTokenProvider.createRefreshToken(memberId, null);
            member.updateRefreshToken(refreshToken);
            memberRepository.updateRefreshToken(memberId, refreshToken);
        }

        return Auth.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public List<Member> getMembers() {
        return memberRepository.findAll();
    }
}
