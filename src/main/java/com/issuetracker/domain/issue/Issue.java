package com.issuetracker.domain.issue;

import com.issuetracker.domain.comment.Comment;
import com.issuetracker.domain.common.BaseDateTime;
import com.issuetracker.domain.milestone.Milestone;
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
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Table("ISSUE")
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
    private Set<Assignee> assignees = new HashSet<>();

    @Builder.Default
    @MappedCollection(idColumn = "ISSUE_ID")
    private Set<IssueLabel> issueLabels = new HashSet<>();

    @Column("MILESTONE_ID")
    private AggregateReference<Milestone, String> milestoneRef;

    public Issue updateStatus(boolean status) {
        this.isOpen = status;
        return this;
    }

    public void addComment(Comment comment) {
        comment.initBaseDateTime();
        this.comments.add(comment);
    }

    public void addAssignee(String memberId) {
        Assignee ref = Assignee.of(memberId);
        assignees.add(ref);
    }

    public void addAssignees(List<String> memberIds) {
        memberIds.forEach(this::addAssignee);
    }

    public void deleteAssignee(String memberId) {
        Assignee ref = Assignee.of(memberId);
        assignees.remove(ref);
    }

    public void addLabel(String labelId) {
        IssueLabel ref = IssueLabel.of(labelId);

        issueLabels.add(ref);
    }

    public void addLabels(List<String> labelIds) {
        labelIds.forEach(this::addLabel);
    }

    public void deleteLabel(String labelId) {
        IssueLabel ref = IssueLabel.of(labelId);

        issueLabels.remove(ref);
    }

    public void assignMilestone(String milestoneId) {
        milestoneRef = AggregateReference.to(milestoneId);
    }

    public void deleteMilestone() {
        milestoneRef = null;
    }
}
