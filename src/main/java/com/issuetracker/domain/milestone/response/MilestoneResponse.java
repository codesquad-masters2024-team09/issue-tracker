package com.issuetracker.domain.milestone.response;

import com.issuetracker.domain.milestone.Milestone;
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

    public static MilestoneResponse of(Milestone milestone) {
        return MilestoneResponse.builder()
                .id(milestone.getId())
                .dueDate(milestone.getDueDate())
                .description(milestone.getDescription())
                .build();
    }
}
