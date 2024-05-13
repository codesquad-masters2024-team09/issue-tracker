package com.issuetracker.domain.comment;

import com.issuetracker.domain.comment.request.CommentCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> create(@Validated @RequestBody CommentCreateRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("검증 에러={}", bindingResult.getFieldError("content").getDefaultMessage());
            return ResponseEntity.badRequest().body(bindingResult.getFieldError("content").getDefaultMessage());
        }

        return ResponseEntity.ok(commentService.create(request));
    }
}
