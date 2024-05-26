package com.issuetracker.domain.issue;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IssueViewMapper {

    IssueDetails findById(Long issueId);
}
