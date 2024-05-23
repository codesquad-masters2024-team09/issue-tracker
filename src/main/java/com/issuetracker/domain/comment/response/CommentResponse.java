package com.issuetracker.domain.comment.response;

import com.issuetracker.domain.comment.Comment;
import com.issuetracker.domain.common.LocalDateTimeToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private String memberId;
    private String content;
    private String createdAt;

    public static CommentResponse of(Comment comment) {
        LocalDateTime now = LocalDateTime.now();
        String createdAt = LocalDateTimeToStringConverter.convert(now, comment.getCreatedAt());

        return CommentResponse.builder()
                .id(comment.getId())
                .memberId(comment.getMemberId())
                .content(comment.getContent())
                .createdAt(createdAt)
                .build();
    }
}
