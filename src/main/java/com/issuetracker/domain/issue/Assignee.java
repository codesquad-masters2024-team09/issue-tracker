package com.issuetracker.domain.issue;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Table("ASSIGNEE")
public class Assignee {

    private String memberId;

    protected static Assignee of(String memberId) {
        return new Assignee(memberId);
    }
}
