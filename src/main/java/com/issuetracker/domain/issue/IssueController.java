package com.issuetracker.domain.issue;

import com.issuetracker.domain.issue.request.*;
import com.issuetracker.domain.issue.response.IssueDetailsResponse;
import com.issuetracker.domain.issue.response.IssueListResponse;
import com.issuetracker.domain.issue.response.IssueStatusResponse;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
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

    @PostMapping("/{issueId}/assignee")
    public ResponseEntity<Void> addAssignee(@PathVariable("issueId") Long issueId,
                                            @Valid @RequestBody MemberAssignRequest memberAssignRequest) {
        issueService.addAssignee(issueId, memberAssignRequest.getMemberId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{issueId}/assignee")
    public ResponseEntity<Void> deleteAssignee(@PathVariable("issueId") Long issueId,
                                               @Valid @RequestBody MemberAssignRequest memberAssignRequest) {
        issueService.deleteAssignee(issueId, memberAssignRequest.getMemberId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/label")
    public ResponseEntity<Void> addLabel(@PathVariable("issueId") Long issueId,
                                         @Valid @RequestBody LabelAssignRequest labelAssignRequest) {
        issueService.addLabel(issueId, labelAssignRequest.getLabelId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{issueId}/label")
    public ResponseEntity<?> deleteLabel(@PathVariable("issueId") Long issueId,
                                         @Valid @RequestBody LabelAssignRequest LabelAssignRequest) {
        issueService.deleteLabel(issueId, LabelAssignRequest.getLabelId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{issueId}/milestone")
    public ResponseEntity<Void> assignMilestone(@PathVariable("issueId") Long issueId,
                                                @Valid @RequestBody MilestoneAssignRequest milestoneAssignRequest) {
        issueService.assignMilestone(issueId, milestoneAssignRequest.getMilestoneId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{issueId}/milestone")
    public ResponseEntity<?> deleteMilestone(@PathVariable("issueId") Long issueId) {
        issueService.deleteMilestone(issueId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<IssueDetailsResponse> getDetail(@PathVariable("issueId") Long issueId) {
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

    @PatchMapping("/status")
    public ResponseEntity<IssueStatusResponse> updateStatus(
            @RequestParam("issueId") String issueIdString, @RequestParam("isOpen") boolean openStatus) {
        issueService.updateStatus(issueIdString, openStatus);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<IssueListResponse> getIssuesByCondition(
            @RequestParam(required = false, defaultValue ="is:open", name = "q") String condition) {
        return ResponseEntity.ok(
                issueService.getIssuesByCondition(IssueSearchCondition.of(condition)));
    }
}
