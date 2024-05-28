package com.issuetracker.domain.milestone.response;

import com.issuetracker.domain.milestone.MilestoneDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class MilestoneResponse {

    private String id;
    private LocalDate dueDate;
    private String description;
    private boolean isOpen;
    private Integer progress;
    private Integer openIssues;
    private Integer closeIssues;

    public static MilestoneResponse of(MilestoneDetails milestone) {
        Integer totalIssues = milestone.getTotalIssues();
        Integer openIssues = milestone.getOpenIssues();
        int closeIssues = totalIssues - openIssues;
        int progress = closeIssues > 0 ? closeIssues / totalIssues * 100 : 0;

        return MilestoneResponse.builder()
                .id(milestone.getId())
                .dueDate(milestone.getDueDate())
                .description(milestone.getDescription())
                .isOpen(milestone.isOpen())
                .progress(progress)
                .openIssues(openIssues)
                .closeIssues(closeIssues)
                .build();
    }
}
