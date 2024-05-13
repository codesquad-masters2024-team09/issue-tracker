package com.issuetracker.domain.comment;

import com.issuetracker.domain.comment.request.CommentCreateRequest;
import com.issuetracker.domain.issue.Issue;
import com.issuetracker.domain.issue.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final IssueRepository issueRepository;

    public Long create(CommentCreateRequest request) {
        Issue issue = issueRepository.findById(request.getIssueId())
                .orElseThrow(IllegalStateException::new);

        Comment comment = request.toEntity();

        issue.getComments().add(comment);
        issueRepository.save(issue);

        return comment.getId();
    }
}
