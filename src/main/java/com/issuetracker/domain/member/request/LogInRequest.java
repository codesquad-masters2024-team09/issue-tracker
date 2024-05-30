package com.issuetracker.domain.member.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogInRequest {

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "특수문자나 한글은 사용할 수 없습니다.")
    private String memberId;
    private String password;
}
