package com.issuetracker.domain.milestone;

import com.issuetracker.domain.milestone.request.MilestoneCreateRequest;
import com.issuetracker.domain.milestone.request.MilestoneUpdateRequest;
import com.issuetracker.domain.milestone.response.MilestoneListResponse;
import com.issuetracker.global.exception.milestone.MilestoneDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final MilestoneViewMapper milestoneViewMapper;

    public String create(MilestoneCreateRequest request) {
        if (milestoneRepository.existsById(request.getId())) {
            throw new MilestoneDuplicateException();
        }

        Milestone milestone = request.toEntity();
        Milestone savedMilestone = milestoneRepository.save(milestone);
        return savedMilestone.getId();
    }

//    public String create(MilestoneCreateRequest request) {
//        Milestone milestone = request.toEntity();
//        try {
//            Milestone savedMilestone = milestoneRepository.save(milestone);
//            return savedMilestone.getId();
//        } catch (DbActionExecutionException ex) {
//            throw new MilestoneDuplicateException();
//        }
//    }

    @Transactional(readOnly = true)
    public MilestoneListResponse getMilestones(boolean openStatus) {
        List<MilestoneDetails> milestones = milestoneViewMapper.findAllByOpenStatus(openStatus);
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
}
