package com.issuetracker.domain.milestone;

import com.issuetracker.domain.milestone.request.MilestoneCreateRequest;
import com.issuetracker.domain.milestone.request.MilestoneUpdateRequest;
import com.issuetracker.domain.milestone.response.MilestoneListResponse;
import com.issuetracker.domain.milestone.response.MilestoneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final MilestoneMapper milestoneMapper;

    public MilestoneResponse create(MilestoneCreateRequest request) {
        Milestone milestone = request.toEntity();
        Milestone savedMilestone = milestoneRepository.save(milestone);
        return MilestoneResponse.of(savedMilestone);
    }

    public MilestoneListResponse getMilestones(boolean openStatus) {
        List<Milestone> milestones =  milestoneRepository.findMilestonesByIsOpen(openStatus);
        return MilestoneListResponse.of(milestones);
    }

    public void delete(String milestoneId) {
        milestoneRepository.deleteById(milestoneId);
    }

    public void edit(String milestoneId, MilestoneUpdateRequest form) {
        if (form.getId() == null && form.getDueDate() == null && form.getDescription() == null) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("milestoneId", milestoneId);
        requestMap.put("form", form);

        milestoneMapper.update(requestMap);
    }

    public Long count(boolean openStatus) {
        return milestoneRepository.countByIsOpen(openStatus);
    }


    public void updateStatus(String milestoneId, boolean desiredState) {
        milestoneRepository.updateOpenStatus(milestoneId, desiredState);
    }
}
