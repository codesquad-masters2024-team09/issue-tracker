package com.issuetracker.domain.comment.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequest {

    @NotNull(message = "댓글은 빈 값일 수 없습니다")
    @Size(max = 2000, message = "댓글의 길이는 0에서 2000 사이여야 합니다")
    private String content;
}
