package com.issuetracker.domain.auth.response;

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
