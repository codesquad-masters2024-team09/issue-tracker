package com.issuetracker.domain.issue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.issuetracker.domain.comment.Comment;
import com.issuetracker.domain.label.Label;
import com.issuetracker.domain.label.LabelRepository;
import com.issuetracker.domain.member.Member;
import com.issuetracker.domain.member.MemberRepository;
import com.issuetracker.domain.milestone.Milestone;
import com.issuetracker.domain.milestone.MilestoneRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class IssueTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .id("tester")
                .password("123")
                .build();

        testMember = memberRepository.save(member);
    }

    @Test
    @DisplayName("작성자, 제목, 내용이 모두 들어있는 이슈는 등록에 성공한다")
    void create_success() {
        // given
        Issue issue = makePureIssue();

        // when
        Issue savedIssue = issueRepository.save(issue);

        // then
        assertThat(savedIssue).isEqualTo(issue);
        assertThat(savedIssue.getMemberId()).isEqualTo(testMember.getId());
        assertThat(savedIssue.getCreatedAt()).isNotNull();
        assertThat(savedIssue.getCreatedAt()).isEqualTo(savedIssue.getModifiedAt());
    }

    @Test
    @DisplayName("사용자 아이디가 없으면 이슈 등록에 실패한다.")
    void create_fail() {
        // given
        Issue issue = Issue.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        // when & then
        assertThatThrownBy(() -> issueRepository.save(issue))
                .isInstanceOf(DbActionExecutionException.class);   // TODO: Exception 클래스 변경, CustomException 만들기
    }

    @Test
    @DisplayName("이슈를 생성할 때 데이터베이스에 저장된 서로 다른 레이블 2개를 추가할 수 있다")
    void addLabel() {
        // given
        Label document = makeLabel("document");
        Label bug = makeLabel("bug");

        Label savedDocument = labelRepository.save(document);
        Label savedBug = labelRepository.save(bug);

        Issue issue = makePureIssue();

        // when
        issue.addLabel(savedDocument);
        issue.addLabel(savedBug);

        Issue savedIssue = issueRepository.save(issue);

        // then
        assertThat(savedIssue.getIssueLabels()).hasSize(2);
        assertThat(savedIssue.getIssueLabels()).extracting("labelId").contains("document", "bug");

        // when
        savedIssue.deleteLabel(savedDocument);
        Issue savedIssueAfterDeleteLabel = issueRepository.save(savedIssue);
        Issue findIssue2 = issueRepository.findById(savedIssueAfterDeleteLabel.getId())
                .orElseThrow(RuntimeException::new);

        List<Label> labels = labelRepository.findAll();

        // then
        assertThat(findIssue2.getIssueLabels()).hasSize(1);
        assertThat(labels).hasSize(2);
    }

    @Test
    @DisplayName("이슈에 지정된 레이블을 제거해도 레이블 테이블의 레이블은 삭제되지 않는다")
    void deleteLabel() {
        // given
        Label document = makeLabel("document");
        Label bug = makeLabel("bug");

        Label savedDocument = labelRepository.save(document);
        Label savedBug = labelRepository.save(bug);

        Issue issue = makePureIssue();

        issue.addLabel(savedDocument);
        issue.addLabel(savedBug);

        Issue savedIssue = issueRepository.save(issue);

        // when
        savedIssue.deleteLabel(savedDocument);
        Issue issueWithDeletedLabel = issueRepository.save(savedIssue);

        List<Label> labels = labelRepository.findAll();

        // then
        assertThat(issueWithDeletedLabel.getIssueLabels()).hasSize(1);
        assertThat(labels).hasSize(2);
    }

    @Test
    @DisplayName("이슈에 댓글 3개를 추가할 수 있다")
    void addComment() {
        // given
        Issue issue = makePureIssue();

        Issue savedIssue = issueRepository.save(issue);

        for (int i = 0; i < 3; i++) {
            Comment comment = Comment.builder()
                    .issue_id(savedIssue.getId())
                    .member_id(testMember.getId())
                    .content("test comment" + i)
                    .build();

            issue.addComment(comment);
        }

        issueRepository.save(issue);

        // when
        Issue findIssue = issueRepository.findById(issue.getId()).orElseThrow(RuntimeException::new);

        // then
        assertThat(findIssue.getComments())
                .extracting("content")
                .contains("test comment0", "test comment1", "test comment2");
        assertThat(findIssue.getComments()).hasSize(3);
    }


    @Test
    @DisplayName("이슈에 데이터베이스에 저장된 마일스톤을 추가할 수 있다")
    void addMilestone() {
        // given
        Issue issue = makePureIssue();

        Milestone milestone = Milestone.builder()
                .id("first milestone")
                .description("test content")
                .build();

        Milestone savedMilestone = milestoneRepository.save(milestone);
        issue.assignMilestone(savedMilestone);

        // when
        Issue savedIssue = issueRepository.save(issue);

        // then
        assertThat(savedIssue.getMilestoneRef().getId()).isEqualTo(savedMilestone.getId());
        assertThat(savedIssue.getMilestoneRef().getId()).isEqualTo("first milestone");
    }

    @Test
    @DisplayName("이슈에 지정된 마일스톤을 제거해도 마일스톤 테이블의 마일스톤이 제거되지 않는다")
    void deleteMilestoneOnIssue() {
        // given
        Issue issue = makePureIssue();

        Milestone milestone = Milestone.builder()
                .id("first milestone")
                .description("test content")
                .build();

        Milestone savedMilestone = milestoneRepository.save(milestone);
        issue.assignMilestone(savedMilestone);

        Issue savedIssue = issueRepository.save(issue);

        // when
        savedIssue.deleteMilestone();
        Issue issueWithNoMilestone = issueRepository.save(issue);

        Milestone findMilestone = milestoneRepository.findById("first milestone").orElseThrow(RuntimeException::new);

        // then
        assertThat(issueWithNoMilestone.getMilestoneRef()).isNull();
        assertThat(findMilestone.getId()).isEqualTo("first milestone");
    }

    private Label makeLabel(String labelName) {
        return Label.builder()
                .id(labelName)
                .colorCode("#ffffff")
                .textColor("#000000")
                .build();
    }

    private Issue makePureIssue() {
        return Issue.builder()
                .memberId(testMember.getId())
                .title("testTitle")
                .content("testContent")
                .build();
    }
}
