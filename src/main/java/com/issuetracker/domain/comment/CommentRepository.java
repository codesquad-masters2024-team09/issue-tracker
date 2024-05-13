package com.issuetracker.domain.comment;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Modifying
    @Query("UPDATE COMMENT SET CONTENT = :content WHERE COMMENT_ID = :commentId;")
    void update(Long commentId, String content);
}
