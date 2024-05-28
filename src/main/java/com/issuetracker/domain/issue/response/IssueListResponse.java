package com.issuetracker.domain.issue.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IssueListResponse {

    private int openIssues;
    private int closedIssues;
    private List<IssuePreviewResponse> issues;

    public static IssueListResponse from(IssueCount issueCount, List<SimpleIssue> simpleIssues) {
        return IssueListResponse.builder()
                .openIssues(issueCount.getOpenIssues())
                .closedIssues(issueCount.getClosedIssues())
                .issues(simpleIssues.stream().map(IssuePreviewResponse::from).toList())
                .build();
    }
}
