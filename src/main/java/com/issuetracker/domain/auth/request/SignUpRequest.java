package com.issuetracker.domain.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank
    private String profileImgUrl;

    @NotBlank
    private String memberId;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
}
