package com.issuetracker.domain.issue.response;

import com.issuetracker.domain.issue.Issue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IssueStatusResponse {

    private boolean isOpen;

    public static IssueStatusResponse from(Issue issue) {
        return new IssueStatusResponse(issue.isOpen());
    }
}
