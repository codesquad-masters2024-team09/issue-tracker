package com.issuetracker.domain.issue.response;

import com.issuetracker.domain.comment.response.CommentResponse;
import com.issuetracker.domain.common.LocalDateTimeToStringConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.issuetracker.domain.label.response.LabelResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IssueDetailsResponse {

    private Long id;
    private String memberId;
    private String title;
    private String content;

    @Builder.Default
    private List<CommentResponse> comments = new ArrayList<>();

    @Builder.Default
    private List<LabelResponse> labels = new ArrayList<>();
    private String milestoneId;
    private Integer milestoneProgress;
    private String createdAt;

    public static IssueDetailsResponse of(IssueDetails issueDetails) {
        return IssueDetailsResponse.builder()
                .id(issueDetails.getId())
                .memberId(issueDetails.getMemberId())
                .title(issueDetails.getTitle())
                .content(issueDetails.getContent())
                .comments(issueDetails.getComments().stream().map(CommentResponse::of).toList())
                .labels(issueDetails.getLabels().stream().map(LabelResponse::of).toList())
                .milestoneId(issueDetails.getMilestoneId())
                .milestoneProgress(issueDetails.getMilestoneProgress())
                .createdAt(LocalDateTimeToStringConverter.convert(LocalDateTime.now(), issueDetails.getCreatedAt()))
                .build();
    }
}