package com.issuetracker.domain.milestone;

import com.issuetracker.domain.milestone.request.MilestoneUpdateRequest;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestoneRepository extends CrudRepository<Milestone, String> {

    List<Milestone> findMilestonesByIsOpen(boolean openStatus);
    Long countByIsOpen(boolean openStatus);

    @Modifying
    @Query("UPDATE MILESTONE SET IS_OPEN = :desiredState WHERE MILESTONE_ID = :milestoneId")
    void updateOpenStatus(String milestoneId, boolean desiredState);


    @Modifying
    @Query("UPDATE MILESTONE SET MILESTONE_ID = :#{#form.id}, DUE_DATE = :#{#form.dueDate}, DESCRIPTION = :#{#form.description} " +
            "WHERE MILESTONE_ID = :milestoneId")
    void updateMilestoneBy(String milestoneId, MilestoneUpdateRequest form);
}
