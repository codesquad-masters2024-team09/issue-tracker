package com.issuetracker.domain.issue;

import com.issuetracker.domain.common.BaseDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table("ISSUE_LABEL")
public class IssueLabel extends BaseDateTime {

    private String labelId;
}
