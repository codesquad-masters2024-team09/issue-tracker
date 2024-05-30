package com.issuetracker.domain.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Auth {

    private final String accessToken;
    private final String refreshToken;
}
