package com.issuetracker.domain.milestone;

import com.issuetracker.domain.milestone.request.MilestoneCreateRequest;
import com.issuetracker.domain.milestone.request.MilestoneUpdateRequest;
import com.issuetracker.domain.milestone.response.MilestoneProcessResponse;
import com.issuetracker.domain.milestone.response.MilestoneListResponse;
import com.issuetracker.domain.milestone.response.MilestoneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;

    private static final Integer OPENED_ISSUE_NUMBER = 1;

    public MilestoneResponse create(MilestoneCreateRequest request) {
        Milestone milestone = request.toEntity();
        Milestone savedMilestone = milestoneRepository.save(milestone);
        return MilestoneResponse.of(savedMilestone);
    }

    @Transactional(readOnly = true)
    public MilestoneListResponse getMilestones(boolean openStatus) {
        List<Milestone> milestones =  milestoneRepository.findMilestonesByIsOpenOrderByCreatedAtDesc(openStatus);
        return MilestoneListResponse.of(milestones);
    }

    public void delete(String milestoneId) {
        milestoneRepository.deleteById(milestoneId);
    }

    public void edit(String milestoneId, MilestoneUpdateRequest form) {
        if (form.getId() == null && form.getDueDate() == null && form.getDescription() == null) {
            throw new IllegalArgumentException();
        }
        milestoneRepository.updateMilestoneBy(milestoneId, form);
    }

    @Transactional(readOnly = true)
    public Long count(boolean openStatus) {
        return milestoneRepository.countByIsOpen(openStatus);
    }

    public void updateStatus(String milestoneId, boolean desiredState) {
        milestoneRepository.updateOpenStatus(milestoneId, desiredState);
    }

    @Transactional(readOnly = true)
    public MilestoneProcessResponse calculateMilestoneProcess(String milestoneId) {
        List<Integer> metric = milestoneRepository.getIssueMetric(milestoneId);
        long openedIssueCount = metric.stream().filter(v -> v.equals(OPENED_ISSUE_NUMBER)).count();
        return MilestoneProcessResponse.of(openedIssueCount, metric.size()-openedIssueCount);
    }
}
