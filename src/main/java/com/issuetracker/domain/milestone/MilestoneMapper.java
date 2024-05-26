package com.issuetracker.domain.milestone;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MilestoneMapper {

    void update(Map<String, Object> requestMap);

    String findById(String id);
}
