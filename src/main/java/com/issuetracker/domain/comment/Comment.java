package com.issuetracker.domain.comment;

import com.issuetracker.domain.common.BaseDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Comment extends BaseDateTime {

    @Id
    @Column("COMMENT_ID")
    private Long id;
    private String memberId;
    private Long issueId;
    private String content;
}
