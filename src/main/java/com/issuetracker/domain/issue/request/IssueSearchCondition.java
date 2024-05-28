package com.issuetracker.domain.issue.request;

import com.issuetracker.domain.issue.util.IssueQueryParser;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueSearchCondition {

    private String author;
    private List<String> labelIds;
    private String milestoneId;
    private boolean isOpen = true;
    private List<String> assignees;
    private String keyword;

    public static IssueSearchCondition of(String requestQueryString) {
        List<String> queryString = IssueQueryParser.parseQueryString(requestQueryString);

        String keyword = IssueQueryParser.parseKeyword(queryString);
        String author = IssueQueryParser.parseAuthor(queryString);
        List<String> labelIds = IssueQueryParser.parseLabels(queryString);
        String milestone = IssueQueryParser.parseMilestone(queryString);
        List<String> assignees = IssueQueryParser.parseAssignees(queryString);
        boolean isOpen = IssueQueryParser.parseOpenStatus(queryString);

        return IssueSearchCondition.builder()
                .author(author)
                .labelIds(labelIds)
                .milestoneId(milestone)
                .isOpen(isOpen)
                .assignees(assignees)
                .keyword(keyword)
                .build();
    }
}
