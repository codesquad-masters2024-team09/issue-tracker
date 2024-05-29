package com.issuetracker.domain.member.response;

import com.issuetracker.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MemberListResponse {

    private List<MemberResponse> members;

    public static MemberListResponse from(List<Member> members) {
        return MemberListResponse.builder()
                .members(members.stream().map(MemberResponse::from).collect(Collectors.toList()))
                .build();
    }
}
