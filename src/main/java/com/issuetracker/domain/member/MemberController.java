package com.issuetracker.domain.member;

import com.issuetracker.domain.member.request.LogoutRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        memberService.logout(request);
        return expireCookieResponse();
    }

    private ResponseEntity<Void> expireCookieResponse() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, String.format(
                "%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=None",  // TODO: SameSite 설정 변경
                "refreshToken", null, "/", 0));

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .build();
    }
}
