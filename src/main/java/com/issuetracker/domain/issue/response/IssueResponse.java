package com.issuetracker.domain.issue.response;

import com.issuetracker.domain.common.LocalDateTimeToStringConverter;
import com.issuetracker.domain.issue.Issue;
import com.issuetracker.domain.issue.IssueLabel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IssueResponse {

    private Long issueId;
    private String memberId;
    private String title;
    private boolean isOpen;
    private List<String> labelNames;
    private String milestoneName;
    private String createdAt;

    public static IssueResponse of(Issue issue) {
        return IssueResponse.builder()
                .issueId(issue.getId())
                .memberId(issue.getMemberId())
                .title(issue.getTitle())
                .isOpen(issue.isOpen())
                .labelNames(convertToLabelNames(issue.getIssueLabels()))
                .milestoneName(issue.getMilestoneRef() == null ? null : issue.getMilestoneRef().getId())
                .createdAt(LocalDateTimeToStringConverter.convert(issue.getCreatedAt(), LocalDateTime.now()))
                .build();
    }

    private static List<String> convertToLabelNames(Set<IssueLabel> ref) {
        return ref.stream()
                .map(IssueLabel::getLabelId)
                .collect(Collectors.toList());
    }
}
