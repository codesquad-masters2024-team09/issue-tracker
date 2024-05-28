package com.issuetracker.domain.member;

import com.issuetracker.domain.member.request.IdDuplicateCheckRequest;
import com.issuetracker.domain.member.request.LoginRequest;
import com.issuetracker.domain.member.request.SignUpRequest;
import com.issuetracker.domain.member.response.AuthResponse;
import com.issuetracker.domain.member.response.DuplicateCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(memberService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String refreshToken) {
        return ResponseEntity.ok(memberService.refresh(refreshToken));
    }
}
