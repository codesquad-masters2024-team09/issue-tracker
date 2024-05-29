package com.issuetracker.domain.member.response;

import com.issuetracker.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {

    private String memberId;
    private String profileImgUrl;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .memberId(member.getId())
                .profileImgUrl(member.getProfileImgUrl())
                .build();
    }
}
