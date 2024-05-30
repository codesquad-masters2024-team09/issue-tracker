package com.issuetracker.domain.milestone;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MilestoneViewMapper {

    List<MilestoneDetails> findAllByOpenStatus(boolean openStatus);
    List<MilestoneDetails> findAll();
}
