package com.issuetracker.domain.issue.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.issuetracker.domain.issue.util.IssueQueryParser.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueSearchCondition {

    private String author;
    private List<String> labelIds;
    private String milestoneId;

    @Builder.Default
    private boolean isOpen = true;
    private List<String> assignees;
    private String keyword;
    private boolean noMilestone;
    private boolean noAssignee;

    public static IssueSearchCondition of(String requestQueryString) {
        List<String> queryString = parseQueryString(requestQueryString);

        String keyword = parseKeyword(queryString);
        String author = parseAuthor(queryString);
        List<String> labelIds = parseLabels(queryString);
        String milestone = parseMilestone(queryString);
        List<String> assignees = parseAssignees(queryString);
        boolean isOpen = parseOpenStatus(queryString);
        boolean noMilestone = parseNoXXX(queryString, NO_MILESTONE_PATTERN.getPattern());
        boolean noAssignee = parseNoXXX(queryString, NO_ASSIGNEE_PATTERN.getPattern());

        return IssueSearchCondition.builder()
                .author(author)
                .labelIds(labelIds)
                .milestoneId(noMilestone ? null : milestone)
                .isOpen(isOpen)
                .assignees(noAssignee ? null : assignees)
                .keyword(keyword)
                .noAssignee(noAssignee)
                .noMilestone(noMilestone)
                .build();
    }
}
