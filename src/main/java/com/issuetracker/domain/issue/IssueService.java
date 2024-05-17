package com.issuetracker.domain.issue;

import com.issuetracker.domain.issue.request.IssueCreateRequest;
import com.issuetracker.domain.issue.request.IssueUpdateRequest;
import lombok.RequiredArgsConstructor;
import com.issuetracker.domain.issue.response.IssueDetailResponse;
import org.springframework.stereotype.Service;
import com.issuetracker.domain.label.Label;
import com.issuetracker.domain.milestone.Milestone;
import java.util.HashMap;
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

    public Long create(IssueCreateRequest request) {
        Issue issue = request.toEntity();
        Issue savedIssue = issueRepository.save(issue);
        return savedIssue.getId();
    }

    public IssueDetailResponse getDetail(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        return IssueDetailResponse.from(issue);
    }

    public void delete(Long issueId) {
        issueRepository.deleteById(issueId);
    }

    public void edit(Long issueId, IssueUpdateRequest form) {
        if (form.getTitle() == null && form.getContent() == null) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("issueId", issueId);
        requestMap.put("form", form);

        issueMapper.update(requestMap);
    }

    public IssueListResponse getIssues() {
        List<Issue> issues = issueRepository.findAll();
        return IssueListResponse.of(
                issues.stream().map(IssueResponse::of).collect(Collectors.toList())
        );
    }

    public Issue addLabel(Long issueId, Label label) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        issue.addLabel(label);

        return issueRepository.save(issue);
    }

    public Issue addLabels(Long issueId, List<Label> labels) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        issue.addLabels(labels);

        return issueRepository.save(issue);
    }

    public Issue deleteLabel(Long issueId, Label label) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        issue.deleteLabel(label);

        return issueRepository.save(issue);
    }

    public Issue assignMilestone(Long issueId, Milestone milestone) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        issue.assignMilestone(milestone);

        return issueRepository.save(issue);
    }

    public Issue deleteMilestone(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(RuntimeException::new);
        issue.deleteMilestone();

        return issueRepository.save(issue);
    }
}