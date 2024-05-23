package com.issuetracker.domain.issue;

import com.issuetracker.domain.issue.request.IssueSearchCondition;
import java.util.HashMap;

import com.issuetracker.domain.issue.response.IssueDetailsResponse;
import lombok.RequiredArgsConstructor;
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
    private final IssueViewMapper issueViewMapper;

    public Long create(Issue issue) {
        Issue savedIssue = issueRepository.save(issue);
        return savedIssue.getId();
    }

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

    public IssueListResponse getIssues() {
        List<Issue> issues = issueRepository.findAll();
        return IssueListResponse.of(
                issues.stream().map(IssueResponse::of).collect(Collectors.toList())
        );
    }

    public void addLabel(Long issueId, String labelId) {
        // TODO: 레이블이 존재하는지 확인하는 로직 넣어야 함
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        issue.addLabel(labelId);

        issueRepository.save(issue);
    }

    public Issue addLabels(Long issueId, List<String> labelIds) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        issue.addLabels(labelIds);

        return issueRepository.save(issue);
    }

    public Issue deleteLabel(Long issueId, String labelId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        issue.deleteLabel(labelId);

        return issueRepository.save(issue);
    }

    public void assignMilestone(Long issueId, String milestoneId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        issue.assignMilestone(milestoneId);

        issueRepository.save(issue);
    }

    public Issue deleteMilestone(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
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