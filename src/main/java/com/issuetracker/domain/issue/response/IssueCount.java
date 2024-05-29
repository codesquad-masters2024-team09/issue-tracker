package com.issuetracker.domain.issue.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IssueCount {

    private int openIssues;
    private int closedIssues;
    private int totalIssues;
}
