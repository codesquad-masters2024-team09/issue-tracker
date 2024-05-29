package com.issuetracker.domain.member;

import com.issuetracker.domain.member.response.MemberListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberListResponse> getMembers() {
        List<Member> members = memberService.getMembers();
        MemberListResponse memberListResponse = MemberListResponse.from(members);

        return ResponseEntity.ok(memberListResponse);
    }
}
