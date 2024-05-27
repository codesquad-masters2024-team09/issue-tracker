package com.issuetracker.domain.auth;

import com.issuetracker.domain.auth.request.LoginRequest;
import com.issuetracker.domain.auth.request.LogoutRequest;
import com.issuetracker.domain.auth.request.SignUpRequest;
import com.issuetracker.domain.auth.response.AuthResponse;
import com.issuetracker.domain.member.Member;
import com.issuetracker.domain.member.MemberRepository;
import com.issuetracker.global.exception.member.InvalidLoginDataException;
import com.issuetracker.global.exception.member.MemberDuplicateException;
import com.issuetracker.global.exception.member.MemberNotFoundException;
import com.issuetracker.global.security.JwtTokenProvider;
import com.issuetracker.global.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

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

        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), null);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), null);
        member.updateRefreshToken(refreshToken);
        Member savedMember = memberRepository.save(member);

        return AuthResponse.builder()
                .memberId(savedMember.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(InvalidLoginDataException::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getEncodedPassword())) {
            throw new InvalidLoginDataException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), null);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), null);
        member.updateRefreshToken(refreshToken);
        memberRepository.updateRefreshToken(member.getId(), member.getRefreshToken());

        return AuthResponse.builder()
                .memberId(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(LogoutRequest request) {
        Member member = memberRepository.findById(request.getMemberId()).orElseThrow(MemberNotFoundException::new);
        member.expireRefreshToken();
        memberRepository.updateRefreshToken(member.getId(), member.getRefreshToken());
    }
}
