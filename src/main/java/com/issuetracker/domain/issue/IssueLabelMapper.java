package com.issuetracker.domain.issue;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IssueLabelMapper {

    IssueLabel findByIssueId(Long issueId);
}
