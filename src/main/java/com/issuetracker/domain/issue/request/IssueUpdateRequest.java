package com.issuetracker.domain.issue.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueUpdateRequest {

    private String title;
    private String content;

    public boolean validate() {
        return !(title == null && content == null);
    }
}
