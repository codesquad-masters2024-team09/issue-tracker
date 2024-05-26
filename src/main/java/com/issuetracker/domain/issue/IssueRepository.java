package com.issuetracker.domain.issue;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends CrudRepository<Issue, Long> {

    List<Issue> findAll();

    @Modifying
    @Query("UPDATE ISSUE SET IS_OPEN = :openStatus WHERE ISSUE_ID = :issueId")
    void updateOpenStatus(Long issueId, boolean openStatus);
}
