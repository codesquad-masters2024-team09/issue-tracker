package com.issuetracker.domain.issue;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface IssueMapper {

    void update(Map<String, Object> requestMap);
}
