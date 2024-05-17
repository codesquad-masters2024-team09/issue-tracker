package com.issuetracker.domain.issue;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends CrudRepository<Issue, Long> {

    List<Issue> findAll();
}
