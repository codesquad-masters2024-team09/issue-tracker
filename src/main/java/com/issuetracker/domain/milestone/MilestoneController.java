package com.issuetracker.domain.milestone;

import com.issuetracker.domain.milestone.request.MilestoneCreateRequest;
import com.issuetracker.domain.milestone.request.MilestoneUpdateRequest;
import com.issuetracker.domain.milestone.response.MilestoneListResponse;
import com.issuetracker.domain.milestone.response.MilestoneResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/milestones")
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    @GetMapping
    public ResponseEntity<MilestoneListResponse> getMilestones(@RequestParam(value = "isOpen", defaultValue = "true") boolean openStatus) {
        return ResponseEntity
                .ok(milestoneService.getMilestones(openStatus));
    }

    @PostMapping
    public ResponseEntity<MilestoneResponse> create(@Valid @RequestBody MilestoneCreateRequest request) {
        return ResponseEntity
                .ok(milestoneService.create(request));
    }

    @DeleteMapping("/{milestoneId}")
    public ResponseEntity<Void> delete(@PathVariable("milestoneId") String milestoneId) {
        milestoneService.delete(milestoneId);
        return ResponseEntity
                .ok()
                .build();
    }

    @PatchMapping("/{milestoneId}")
    public ResponseEntity<Void> edit(@PathVariable("milestoneId") String milestoneId, @Valid @RequestBody MilestoneUpdateRequest request) {
        milestoneService.edit(milestoneId, request);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/count")
    public ResponseEntity<?> getMilestoneCount(@RequestParam(value = "isOpen", defaultValue = "true") boolean openStatus) {
        return ResponseEntity
                .ok()
                .body(Collections.singletonMap("countResult", milestoneService.count(openStatus)));
    }

    @PatchMapping("/status")
    public ResponseEntity<Void> updateStatus(@RequestParam("milestoneId") String milestoneId, @RequestParam("isOpen") boolean isOpen) {
        milestoneService.updateStatus(milestoneId, isOpen);
        return ResponseEntity
                .ok()
                .build();
    }
}
