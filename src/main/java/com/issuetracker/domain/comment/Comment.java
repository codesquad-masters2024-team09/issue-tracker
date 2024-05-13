package com.issuetracker.domain.comment;

import com.issuetracker.domain.common.BaseDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseDateTime {

    @Id
    @Column("COMMENT_ID")
    private Long id;
    private String member_id;
    private Long issue_id;
    private String content;
}
