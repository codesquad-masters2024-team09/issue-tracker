package com.issuetracker.domain.comment.request;

import static org.assertj.core.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentCreateRequestTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("content 가 2000자를 넘지 않으면서 memberId 필드, issueId가 Null 이면 @NotNull 검증을 통과하지 못한다")
    void validate_fail_when_member_id_is_null_or_issue_id_is_null() {
        // given
        CommentCreateRequest form = CommentCreateRequest.builder().content("test content").build();

        // when
        Set<ConstraintViolation<CommentCreateRequest>> violations = validator.validate(form);

        Iterator<ConstraintViolation<CommentCreateRequest>> iterator = violations.iterator();
        List<String> message = new ArrayList<>();

        while (iterator.hasNext()) {
            ConstraintViolation<CommentCreateRequest> next = iterator.next();
            message.add(next.getMessage());
        }

        // then
        assertThat(message).hasSize(2);
        assertThat(message).contains("멤버 아이디는 빈 값일 수 없습니다", "이슈 아이디는 빈 값일 수 없습니다");
    }

    @Test
    @DisplayName("content 가 2000자를 넘으면 검증을 통과하지 못한다")
    void validate_fail_when_content_size_exceeds() {
        // given
        CommentCreateRequest form = CommentCreateRequest.builder()
                .memberId("tester")
                .issueId(1L)
                .content("t".repeat(2000 + 1))
                .build();

        // when
        Set<ConstraintViolation<CommentCreateRequest>> violations = validator.validate(form);

        Iterator<ConstraintViolation<CommentCreateRequest>> iterator = violations.iterator();
        List<String> message = new ArrayList<>();

        while (iterator.hasNext()) {
            ConstraintViolation<CommentCreateRequest> next = iterator.next();
            message.add(next.getMessage());
        }

        // then
        assertThat(message).hasSize(1);
        assertThat(message).containsOnly("댓글의 길이는 0에서 2000 사이여야 합니다");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 1999, 2000})
    @DisplayName("memberId 필드, issueId 필드가 null 이 아니면서 content 길이가 2000자를 넘지 않으면 검증에 통과한다")
    void validate_success(int size) {
        // given
        CommentCreateRequest form = CommentCreateRequest.builder()
                .memberId("tester")
                .issueId(1L)
                .content("t".repeat(size))
                .build();

        // when
        Set<ConstraintViolation<CommentCreateRequest>> violations = validator.validate(form);

        // then
        assertThat(violations).isEmpty();
    }
}