package com.issuetracker.domain.issue;

import com.issuetracker.domain.comment.Comment;
import com.issuetracker.domain.common.BaseDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Issue extends BaseDateTime {

    @Id
    @Column("ISSUE_ID")
    private Long id;
    private String memberId;
    private String title;
    private String content;

    @Builder.Default
    @MappedCollection(idColumn = "ISSUE_ID", keyColumn = "COMMENT_SEQ")
    private List<Comment> comments = new ArrayList<>();
}
