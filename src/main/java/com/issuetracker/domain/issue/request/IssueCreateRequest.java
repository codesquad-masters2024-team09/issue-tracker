package com.issuetracker.domain.issue.request;

import com.issuetracker.domain.issue.Issue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @NotNull
    private List<String> labels;

    public Issue toEntity() {
        return Issue.builder()
                .memberId(memberId)
                .title(title)
                .content(content)
                .build();
    }
}
