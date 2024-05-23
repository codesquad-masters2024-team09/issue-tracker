package com.issuetracker.domain.issue.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IssueListResponse {

    private List<IssuePreviewResponse> issues;

    public static IssueListResponse from(List<SimpleIssue> simpleIssues) {
        return new IssueListResponse(
                simpleIssues.stream()
                        .map(IssuePreviewResponse::from)
                        .toList()
        );
    }
}
