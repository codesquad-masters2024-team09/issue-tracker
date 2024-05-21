package com.issuetracker.domain.issue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.issuetracker.domain.comment.Comment;
import com.issuetracker.domain.issue.request.IssueSearchCondition;
import com.issuetracker.domain.label.Label;
import com.issuetracker.domain.label.LabelRepository;
import com.issuetracker.domain.member.Member;
import com.issuetracker.domain.member.MemberRepository;
import com.issuetracker.domain.milestone.Milestone;
import com.issuetracker.domain.milestone.MilestoneRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private IssueMapper issueMapper;

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
        issue.addLabel(savedDocument.getId());
        issue.addLabel(savedBug.getId());

        Issue savedIssue = issueRepository.save(issue);

        // then
        assertThat(savedIssue.getIssueLabels()).hasSize(2);
        assertThat(savedIssue.getIssueLabels()).extracting("labelId").contains("document", "bug");

        // when
        savedIssue.deleteLabel(savedDocument.getId());
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

        issue.addLabel(savedDocument.getId());
        issue.addLabel(savedBug.getId());

        Issue savedIssue = issueRepository.save(issue);

        // when
        savedIssue.deleteLabel(savedDocument.getId());
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
                    .issueId(savedIssue.getId())
                    .memberId(testMember.getId())
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
        issue.assignMilestone(savedMilestone.getId());

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
        issue.assignMilestone(savedMilestone.getId());

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

    @Test
    @DisplayName("필터 조건에 따른 동적 쿼리를 처리할 수 있다")
    void findByCondition() {
        // given
        /* Issue 생성: Labels, milestone 지정 */
        Issue issue = makePureIssue();
        Issue issue2 = makePureIssue();

        Issue savedIssue = issueRepository.save(issue);
        Issue savedIssue2 = issueRepository.save(issue2);

        // labels
        Label document = makeLabel("document");
        Label bug = makeLabel("bug");
        Label bugFix = makeLabel("bug fix");
        Label savedDocument = labelRepository.save(document);
        Label savedBug = labelRepository.save(bug);
        Label savedBugFix = labelRepository.save(bugFix);

        savedIssue.addLabel(savedDocument.getId());
        savedIssue.addLabel(savedBug.getId());
        savedIssue.addLabel(savedBugFix.getId());

        savedIssue2.addLabel(savedDocument.getId());
        savedIssue2.addLabel(savedBugFix.getId());

        // milestone
        Milestone milestone = Milestone.builder()
                .id("first milestone")
                .description("test content")
                .build();

        Milestone savedMilestone = milestoneRepository.save(milestone);
        savedIssue.assignMilestone(savedMilestone.getId());

        issueRepository.save(savedIssue);
        issueRepository.save(savedIssue2);

        /* 검색 조건 */
        IssueSearchCondition condition = IssueSearchCondition.builder()
                .labelIds(List.of("bug fix", "document"))
                .milestoneId("first milestone")
                .isOpen(true)
                .build();

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("author", condition.getAuthor());
        conditionMap.put("isOpen", condition.isOpen());
        conditionMap.put("milestoneId", condition.getMilestoneId());
        conditionMap.put("labelIds", condition.getLabelIds());
        conditionMap.put("title", condition.getTitle());

        // when
        List<Issue> issues = issueMapper.findByCondition(conditionMap);

        // then
        assertThat(issues).hasSize(1);
    }
}
