package com.issuetracker.domain.issue;

import com.issuetracker.domain.issue.response.SimpleIssue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IssueViewMapper {

    IssueDetails findById(Long issueId);

    List<SimpleIssue> findAll();
}
