package com.issuetracker.domain.issue.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IssueListResponse {

    private List<IssueResponse> issues;

    public static IssueListResponse of(List<IssueResponse> elements) {
        return new IssueListResponse(elements);
    }
}
