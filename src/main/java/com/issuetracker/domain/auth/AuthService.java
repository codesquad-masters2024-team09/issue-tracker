package com.issuetracker.domain.auth;

import com.issuetracker.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;

    public boolean idDuplicateCheck(String memberId) {
        return memberRepository.existsById(memberId);
    }
}
