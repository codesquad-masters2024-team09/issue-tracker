package com.issuetracker.domain.issue;

import com.issuetracker.domain.issue.request.IssueCreateRequest;
import com.issuetracker.domain.issue.request.IssueSearchCondition;
import com.issuetracker.domain.issue.request.LabelAddRequest;
import com.issuetracker.domain.issue.request.MilestoneAssignRequest;
import com.issuetracker.domain.issue.request.IssueUpdateRequest;
import com.issuetracker.domain.issue.response.IssueDetailResponse;
import com.issuetracker.domain.issue.response.IssueListResponse;
import com.issuetracker.domain.issue.response.IssueResponse;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
                .ok(Collections.singletonMap("issueId", issueService.create(request.toEntity())));
    }

    @PostMapping("/{issueId}/label")
    public ResponseEntity<Void> addLabel(@PathVariable("issueId") Long issueId,
                                         @Valid @RequestBody LabelAddRequest labelAddRequest) {
        issueService.addLabel(issueId, labelAddRequest.getLabelId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/milestone")
    public ResponseEntity<Void> assignMilestone(@PathVariable("issueId") Long issueId,
                                                @Valid @RequestBody MilestoneAssignRequest milestoneAssignRequest) {
        issueService.assignMilestone(issueId, milestoneAssignRequest.getMilestoneId());
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
    public ResponseEntity<Void> edit(@PathVariable("issueId") Long issueId, @Valid @RequestBody IssueUpdateRequest form) {
        if (!form.validate()) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> requestForm = new HashMap<>();
        requestForm.put("issueId", issueId);
        requestForm.put("form", form);

        issueService.edit(requestForm);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping
    public ResponseEntity<?> getIssuesByCondition(@RequestParam("q") String condition) {
        List<Issue> issues = issueService.getIssueByCondition(IssueSearchCondition.of(condition));

        List<IssueResponse> issueResponses = issues.stream()
                .map(IssueResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok(IssueListResponse.of(issueResponses));
    }
}
