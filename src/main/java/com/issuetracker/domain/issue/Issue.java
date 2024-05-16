package com.issuetracker.domain.issue;

import com.issuetracker.domain.comment.Comment;
import com.issuetracker.domain.common.BaseDateTime;
import com.issuetracker.domain.label.Label;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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

    @Builder.Default
    private boolean isOpen = true;

    @Builder.Default
    @MappedCollection(idColumn = "ISSUE_ID")
    private Set<IssueLabel> issueLabels = new HashSet<>();

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void addLabel(Label label) {
        IssueLabel ref = convertToIssueLabel(label);

        issueLabels.add(ref);
    }

    public void addLabels(List<Label> labels) {
        labels.forEach(this::addLabel);
    }

    public void deleteLabel(Label label) {
        IssueLabel ref = convertToIssueLabel(label);

        issueLabels.remove(ref);
    }

    private IssueLabel convertToIssueLabel(Label label) {
        IssueLabel ref = IssueLabel.builder()
                .labelId(label.getId())
                .build();
        return ref;
    }
}
