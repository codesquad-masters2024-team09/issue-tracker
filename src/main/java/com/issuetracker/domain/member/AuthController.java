package com.issuetracker.domain.member;

import com.issuetracker.domain.member.request.IdDuplicateCheckRequest;
import com.issuetracker.domain.member.request.LogInRequest;
import com.issuetracker.domain.member.request.SignUpRequest;
import com.issuetracker.domain.member.response.Auth;
import com.issuetracker.domain.member.response.DuplicateCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static com.issuetracker.global.security.JwtTokenProvider.REFRESH_TOKEN_EXP_TIME;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/id/duplicate")
    public ResponseEntity<DuplicateCheckResponse> idDuplicateCheck(@Valid @RequestBody IdDuplicateCheckRequest request) {
        boolean check = memberService.idDuplicateCheck(request.getMemberId());
        DuplicateCheckResponse response = DuplicateCheckResponse.from(check);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody SignUpRequest request) {
        Auth signup = memberService.signup(request);
        return createAuthResponse(signup);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LogInRequest request) {
        Auth login = memberService.login(request);
        return createAuthResponse(login);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        memberService.logout(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, String.format(
                "%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=None",  // TODO: SameSite 설정 변경
                "refreshToken", null, "/", 0));

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        Auth refresh = memberService.refresh(refreshToken);
        return createAuthResponse(refresh);
    }

    private ResponseEntity<Map<String, String>> createAuthResponse(Auth auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, String.format(
                "%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=None",  // TODO: SameSite 설정 변경
                "refreshToken", auth.getRefreshToken(), "/", REFRESH_TOKEN_EXP_TIME / 1000));

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(Collections.singletonMap("accessToken", auth.getAccessToken()));
    }
}
