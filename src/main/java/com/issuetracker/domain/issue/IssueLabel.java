package com.issuetracker.domain.issue;

import com.issuetracker.domain.common.BaseDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Table("ISSUE_LABEL")
public class IssueLabel extends BaseDateTime {

    private String labelId;

    protected static IssueLabel of(String labelId) {
        return new IssueLabel(labelId);
    }
}
