package com.issuetracker.domain.issue;

import com.issuetracker.domain.issue.request.IssueCreateRequest;
import com.issuetracker.domain.issue.request.IssueLabelCreateRequest;
import com.issuetracker.domain.issue.request.IssueUpdateRequest;
import com.issuetracker.domain.issue.response.IssueDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody IssueCreateRequest request) {
        return ResponseEntity
                .ok(Collections.singletonMap("issueId", issueService.create(request)));
    }

    @PostMapping("/{issueId}")
    public ResponseEntity<Void> addLabel(@PathVariable("issueId") Long issueId,
                                      @Valid @RequestBody IssueLabelCreateRequest issueLabelCreateRequest) {
        issueService.addLabel(issueId, issueLabelCreateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<IssueDetailResponse> getDetail(@PathVariable("issueId") Long issueId) {
        return ResponseEntity
                .ok(issueService.getDetail(issueId));
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<Void> delete(@PathVariable("issueId") Long issueId) {
        issueService.delete(issueId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{issueId}")
    public ResponseEntity<Void> edit(@PathVariable("issueId") Long issueId, @Valid @RequestBody IssueUpdateRequest request) {
        issueService.edit(issueId, request);
        return ResponseEntity
                .ok()
                .build();
    }
}
