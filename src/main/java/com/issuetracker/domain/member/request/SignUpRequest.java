package com.issuetracker.domain.member.request;

import com.issuetracker.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    private String profileImgUrl;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "특수문자나 한글은 사용할 수 없습니다.")
    private String memberId;

    @NotBlank
    private String password;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .id(memberId)
                .encodedPassword(encodedPassword)
                .build();
    }
}
