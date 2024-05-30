package com.issuetracker.domain.milestone;

import com.issuetracker.domain.milestone.response.MilestoneDetails;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MilestoneViewMapper {

    List<MilestoneDetails> findAllByOpenStatus(boolean openStatus);
    List<MilestoneDetails> findAll();
}
