package com.issuetracker.domain.issue.response;

import com.issuetracker.domain.common.LocalDateTimeToStringConverter;
import com.issuetracker.domain.label.response.LabelResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class IssuePreviewResponse {

    private Long issueId;
    private String memberId;
    private String title;
    private boolean isOpen;
    private List<LabelResponse> labels;
    private String milestoneId;
    private String createdAt;

    public static IssuePreviewResponse from(SimpleIssue simpleIssue) {
        return IssuePreviewResponse.builder()
                .issueId(simpleIssue.getIssueId())
                .memberId(simpleIssue.getMemberId())
                .title(simpleIssue.getTitle())
                .isOpen(simpleIssue.isOpen())
                .labels(simpleIssue.getLabels().stream().map(LabelResponse::of).toList())
                .milestoneId(simpleIssue.getMilestoneId())
                .createdAt(LocalDateTimeToStringConverter.convert(simpleIssue.getCreatedAt(), LocalDateTime.now()))
                .build();
    }
}
