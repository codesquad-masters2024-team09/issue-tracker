package com.issuetracker.domain.issue.request;

import com.issuetracker.domain.issue.Issue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IssueCreateRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    @Size(max = 120)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String content;

    private Set<String> labels;

    private String milestoneId;

    public Issue toEntity() {
        Issue issue = Issue.builder()
                .memberId(memberId)
                .title(title)
                .content(content)
                .build();

        if (labels != null && !labels.isEmpty()) {
            labels.forEach(issue::addLabel);
        }

        if (milestoneId != null && !milestoneId.isBlank()) {
            issue.assignMilestone(milestoneId);
        }

        return issue;
    }
}
