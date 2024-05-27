package com.issuetracker.domain.member.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicateCheckResponse {

    private boolean isDuplicated;

    public static DuplicateCheckResponse from(boolean isDuplicated) {
        return new DuplicateCheckResponse(isDuplicated);
    }
}
