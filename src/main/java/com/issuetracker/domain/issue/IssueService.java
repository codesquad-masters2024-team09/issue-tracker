package com.issuetracker.domain.issue;

import com.issuetracker.domain.issue.request.IssueSearchCondition;
import java.util.HashMap;
import com.issuetracker.global.exception.issue.IssueNotFoundException;
import lombok.RequiredArgsConstructor;
import com.issuetracker.domain.issue.response.IssueDetailResponse;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.issuetracker.domain.issue.response.IssueListResponse;
import com.issuetracker.domain.issue.response.IssueResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    public Long create(Issue issue) {
        Issue savedIssue = issueRepository.save(issue);
        return savedIssue.getId();
    }

    public IssueDetailResponse getDetail(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(IssueNotFoundException::new);
        return IssueDetailResponse.from(issue);
    }

    public void delete(Long issueId) {
        issueRepository.deleteById(issueId);
    }

    public void edit(Map<String, Object> form) {
        issueMapper.update(form);
    }

    public IssueListResponse getIssues() {
        List<Issue> issues = issueRepository.findAll();
        return IssueListResponse.of(
                issues.stream().map(IssueResponse::of).collect(Collectors.toList())
        );
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

    public List<Issue> getIssueByCondition(IssueSearchCondition condition) {
        Map<String, Object> conditionMap = new HashMap<>();

        conditionMap.put("author", condition.getAuthor());
        conditionMap.put("isOpen", condition.isOpen());
        conditionMap.put("milestoneId", condition.getMilestoneId());
        conditionMap.put("labelIds", condition.getLabelIds());
        conditionMap.put("title", condition.getTitle());

        return issueMapper.findByCondition(conditionMap);
    }
}