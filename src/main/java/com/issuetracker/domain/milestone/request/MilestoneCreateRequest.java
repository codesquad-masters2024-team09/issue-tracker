package com.issuetracker.domain.milestone.request;

import com.issuetracker.domain.milestone.Milestone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneCreateRequest {

    @NotBlank
    @Size(max = 30)
    private String id;

    private Date dueDate;

    @Size(max = 50)
    private String description;

    public Milestone toEntity() {
        return Milestone.builder()
                .id(id)
                .dueDate(dueDate)
                .description(description)
                .build();
    }
}
