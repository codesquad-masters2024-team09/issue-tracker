package com.issuetracker.domain.issue.response;

import com.issuetracker.domain.comment.Comment;
import com.issuetracker.domain.common.LocalDateTimeToStringConverter;
import com.issuetracker.domain.issue.Issue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IssueDetailResponse {

    private Long id;
    private String memberId;
    private String title;
    private String content;

    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
    private String createdAt;

    public static IssueDetailResponse from(Issue issue) {
        return IssueDetailResponse.builder()
                .id(issue.getId())
                .memberId(issue.getMemberId())
                .title(issue.getTitle())
                .content(issue.getContent())
                .comments(issue.getComments())
                .createdAt(LocalDateTimeToStringConverter.convert(LocalDateTime.now(), issue.getCreatedAt()))
                .build();
    }
}