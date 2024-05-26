package com.issuetracker.domain.auth;

import com.issuetracker.domain.auth.request.IdDuplicateCheckRequest;
import com.issuetracker.domain.auth.response.AuthResponse;
import com.issuetracker.domain.auth.response.DuplicateCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/id/duplicate")
    public ResponseEntity<DuplicateCheckResponse> idDuplicateCheck(@Valid @RequestBody IdDuplicateCheckRequest request) {
        boolean check = authService.idDuplicateCheck(request.getMemberId());
        DuplicateCheckResponse response = DuplicateCheckResponse.from(check);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}
