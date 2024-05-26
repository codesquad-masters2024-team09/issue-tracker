package com.issuetracker.domain.issue;

import com.issuetracker.domain.issue.request.IssueSearchCondition;

import java.util.Arrays;
import java.util.HashMap;

import com.issuetracker.domain.issue.response.IssueDetailsResponse;

import com.issuetracker.domain.issue.response.SimpleIssue;
import com.issuetracker.global.exception.issue.IssueNotFoundException;
import lombok.RequiredArgsConstructor;
import com.issuetracker.domain.issue.response.IssueDetailResponse;
import org.springframework.stereotype.Service;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;
    private final IssueViewMapper issueViewMapper;

    public Long create(Issue issue) {
        Issue savedIssue = issueRepository.save(issue);
        return savedIssue.getId();
    }

    @Transactional(readOnly = true)
    public IssueDetailsResponse getDetail(Long issueId) {
        IssueDetails issueDetails = issueViewMapper.findById(issueId);
        return IssueDetailsResponse.of(issueDetails);
    }

    public void delete(Long issueId) {
        issueRepository.deleteById(issueId);
    }

    public void edit(Map<String, Object> form) {
        issueMapper.update(form);
    }

    public void updateStatus(String issueIdString, boolean openStatus) {
        List<Long> issueIds = Arrays.stream(issueIdString.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();

        issueRepository.updateOpenStatus(issueIds, openStatus);
    }

    public void addLabel(Long issueId, String labelId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(IssueNotFoundException::new);
        issue.addLabel(labelId);

        issueRepository.save(issue);
    }

    public Issue addLabels(Long issueId, List<String> labelIds) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(IssueNotFoundException::new);
        issue.addLabels(labelIds);

        return issueRepository.save(issue);
    }

    public Issue deleteLabel(Long issueId, String labelId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(IssueNotFoundException::new);
        issue.deleteLabel(labelId);

        return issueRepository.save(issue);
    }

    public void assignMilestone(Long issueId, String milestoneId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(IssueNotFoundException::new);
        issue.assignMilestone(milestoneId);

        issueRepository.save(issue);
    }

    public Issue deleteMilestone(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(IssueNotFoundException::new);
        issue.deleteMilestone();

        return issueRepository.save(issue);
    }

    @Transactional(readOnly = true)
    public List<SimpleIssue> getIssuesByCondition(IssueSearchCondition condition) {
        Map<String, Object> conditionMap = new HashMap<>();

        conditionMap.put("author", condition.getAuthor());
        conditionMap.put("isOpen", condition.isOpen());
        conditionMap.put("milestoneId", condition.getMilestoneId());
        conditionMap.put("labelIds", condition.getLabelIds());
        conditionMap.put("title", condition.getTitle()); // TODO: content 추가

        return issueViewMapper.findAllByCondition(conditionMap);
    }
}