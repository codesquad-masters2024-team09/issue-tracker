package com.issuetracker.domain.comment.request;

import com.issuetracker.domain.comment.Comment;
import jakarta.validation.constraints.NotEmpty;
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
public class CommentCreateRequest {

    @NotEmpty(message = "멤버 아이디는 빈 값일 수 없습니다")
    private String memberId;

    @NotNull(message = "이슈 아이디는 빈 값일 수 없습니다")
    private Long issueId;

    @NotNull(message = "댓글은 빈 값일 수 없습니다")
    @Size(max = 2000, message = "댓글의 길이는 0에서 2000 사이여야 합니다")
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .memberId(memberId)
                .issueId(issueId)
                .content(content)
                .build();
    }
}
