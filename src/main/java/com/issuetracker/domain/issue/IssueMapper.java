package com.issuetracker.domain.issue;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface IssueMapper {

    void update(Map<String, Object> requestMap);

    List<Issue> findByCondition(Map<String, Object> condition);
}
