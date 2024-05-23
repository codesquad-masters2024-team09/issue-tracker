package com.issuetracker.domain.milestone.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneUpdateRequest {

    @NotBlank
    @Size(max = 30)
    private String id;
    private LocalDateTime dueDate;
    private String description;
}
