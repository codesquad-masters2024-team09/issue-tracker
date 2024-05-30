package com.issuetracker.domain.member.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleMember {

    private String memberId;
    private String profileImgUrl;
}
