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
class CommentUpdateRequestTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("content 가 2000자를 넘으면 검증을 통과하지 못한다")
    void validate_fail_when_content_size_exceeds() {
        // given
        CommentUpdateRequest form = CommentUpdateRequest.builder()
                .content("t".repeat(2000 + 1))
                .build();

        // when
        Set<ConstraintViolation<CommentUpdateRequest>> violations = validator.validate(form);

        Iterator<ConstraintViolation<CommentUpdateRequest>> iterator = violations.iterator();
        List<String> message = new ArrayList<>();

        while (iterator.hasNext()) {
            ConstraintViolation<CommentUpdateRequest> next = iterator.next();
            message.add(next.getMessage());
        }

        // then
        assertThat(message).hasSize(1);
        assertThat(message).containsOnly("댓글의 길이는 0에서 2000 사이여야 합니다");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 1999, 2000})
    @DisplayName("댓글의 길이가 2000자를 넘지않으면 검증에 통과한다")
    void validate_success(int size) {
        // given
        CommentUpdateRequest form = CommentUpdateRequest.builder()
                .content("t".repeat(size))
                .build();

        // when
        Set<ConstraintViolation<CommentUpdateRequest>> violations = validator.validate(form);

        // then
        assertThat(violations).isEmpty();
    }
}