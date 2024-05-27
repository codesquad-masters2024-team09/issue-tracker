package com.issuetracker.domain.label;

import com.issuetracker.domain.label.request.LabelCreateRequest;
import com.issuetracker.domain.label.request.LabelUpdateRequest;
import com.issuetracker.domain.label.response.LabelListResponse;
import com.issuetracker.domain.label.response.LabelResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/labels")
@RestController
@RequiredArgsConstructor
@Slf4j
public class LabelController {

    private final LabelService labelService;

    @PostMapping
    public ResponseEntity<LabelResponse> create (@Valid @RequestBody LabelCreateRequest labelCreateRequest) {
        return ResponseEntity.ok(
                labelService.create(labelCreateRequest)
        );
    }

    @GetMapping
    public ResponseEntity<LabelListResponse> getLabels() {
        return ResponseEntity.ok(
                labelService.getLabels()
        );
    }

    @PatchMapping("/{labelId}")
    public ResponseEntity<LabelResponse> edit(@PathVariable("labelId") String labelId,
                                              @Valid @RequestBody LabelUpdateRequest labelUpdateRequest) {
        log.info(labelUpdateRequest.getLabelId(), labelUpdateRequest.getTextColor(), labelUpdateRequest.getColorCode());
        return ResponseEntity.ok(
                labelService.edit(labelId, labelUpdateRequest)
        );
    }

    @DeleteMapping("/{labelId}")
    public ResponseEntity<Void> delete(@PathVariable("labelId") String labelId) {
        labelService.delete(labelId);
        return ResponseEntity.ok().build();
    }
}
