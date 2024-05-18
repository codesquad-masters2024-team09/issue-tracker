package com.issuetracker.domain.issue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Table("ISSUE_LABEL")
public class IssueLabel {

    private String labelId;

    protected static IssueLabel of(String labelId) {
        return new IssueLabel(labelId);
    }
}
