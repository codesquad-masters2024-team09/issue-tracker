package com.issuetracker.domain.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IdDuplicateCheckRequest {

    @NotBlank
    private String memberId;
}
