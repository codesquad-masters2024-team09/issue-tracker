package com.issuetracker.domain.auth;

import com.issuetracker.domain.auth.request.IdDuplicateCheckRequest;
import com.issuetracker.domain.auth.request.SignUpRequest;
import com.issuetracker.domain.auth.response.AuthResponse;
import com.issuetracker.domain.auth.response.DuplicateCheckResponse;
import com.issuetracker.domain.member.Member;
import com.issuetracker.global.security.JwtTokenProvider;
import com.issuetracker.global.security.PasswordEncoder;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/id/duplicate")
    public ResponseEntity<DuplicateCheckResponse> idDuplicateCheck(@Valid @RequestBody IdDuplicateCheckRequest request) {
        boolean check = authService.idDuplicateCheck(request.getMemberId());
        DuplicateCheckResponse response = DuplicateCheckResponse.from(check);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignUpRequest request) {
        Member member = request.toEntity(passwordEncoder.encode(request.getPassword()));

        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), null);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), null);

        member.updateRefreshToken(refreshToken);

        String memberId = authService.signup(member);
        AuthResponse authResponse = AuthResponse.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(authResponse);
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
