package com.issuetracker.domain.milestone.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MilestoneDetails {

    private String id;
    private LocalDate dueDate;
    private String description;
    private boolean isOpen;
    private Integer openIssues;
    private Integer totalIssues;
}
