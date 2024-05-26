package com.issuetracker.domain.auth;

import com.issuetracker.domain.member.Member;
import com.issuetracker.domain.member.MemberRepository;
import com.issuetracker.global.exception.member.MemberDuplicateException;
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

    public String signup(Member member) {
        if (idDuplicateCheck(member.getId())) {
            throw new MemberDuplicateException();
        }

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}
