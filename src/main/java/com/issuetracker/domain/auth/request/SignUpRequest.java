package com.issuetracker.domain.auth.request;

import com.issuetracker.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
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
