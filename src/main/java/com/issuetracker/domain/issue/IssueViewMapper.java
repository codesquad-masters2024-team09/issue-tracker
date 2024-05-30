package com.issuetracker.domain.issue;

import com.issuetracker.domain.issue.response.IssueCount;
import com.issuetracker.domain.issue.response.IssueDetails;
import com.issuetracker.domain.issue.response.SimpleIssue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IssueViewMapper {

    IssueDetails findById(Long issueId);

    IssueCount countByCondition(Map<String, Object> conditionMap);

    List<SimpleIssue> findAllByCondition(Map<String, Object> conditionMap);
}
