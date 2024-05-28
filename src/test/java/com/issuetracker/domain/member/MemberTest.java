package com.issuetracker.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자를 등록할 때 createdAt과 modifiedAt이 자동으로 등록된다.")
    void auditBaseDateTime() {
        // given
        Member member = Member.builder()
                .id("tester")
                .encodedPassword("1234")
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember).isEqualTo(member);
        assertThat(savedMember.getCreatedAt()).isNotNull();
        assertThat(savedMember.getCreatedAt()).isEqualTo(savedMember.getModifiedAt());
    }
}
