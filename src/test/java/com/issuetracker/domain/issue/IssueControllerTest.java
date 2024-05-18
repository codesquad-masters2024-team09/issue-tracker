package com.issuetracker.domain.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.domain.issue.request.IssueCreateRequest;
import com.issuetracker.domain.issue.request.IssueLabelCreateRequest;
import com.issuetracker.domain.issue.request.IssueMilestoneCreateRequest;
import com.issuetracker.domain.issue.request.IssueUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import com.issuetracker.domain.issue.response.IssueDetailResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IssueController.class)
class IssueControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueService issueService;

    private final String urlPrefix = "/api/v1";

    @Test
    @DisplayName("이슈를 생성하면 200 상태코드로 생성된 issueId를 응답한다.")
    void create() throws Exception {
      
        // given
        String url = urlPrefix + "/issues";

        IssueCreateRequest request =
                new IssueCreateRequest("testMember", "testTitle", "testContent",
                        List.of("bug", "fix"), "테스트 기능 구현");
        String requestJson = objectMapper.writeValueAsString(request);
        given(issueService.create(any(IssueCreateRequest.class))).willReturn(1L);

        // when
        ResultActions result = mockMvc.perform(
                post(url).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{issueId: 1}"));
    }

    @Test
    @DisplayName("이슈에 레이블 추가를 성공하면 200 OK를 응답한다.")
    void add_label() throws Exception {
        // given
        Long issueId = 1L;
        String url = urlPrefix + "/issues/" + issueId + "/label";

        IssueLabelCreateRequest request = new IssueLabelCreateRequest("bug");
        String requestJson = objectMapper.writeValueAsString(request);
        willDoNothing().given(issueService).addLabel(anyLong(), any(IssueLabelCreateRequest.class));

        // when
        ResultActions result = mockMvc.perform(
                post(url).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("이슈에 마일스톤 추가를 성공하면 200 OK를 응답한다.")
    void assign_milestone() throws Exception {
        // given
        Long issueId = 1L;
        String url = urlPrefix + "/issues/" + issueId + "/milestone";

        IssueMilestoneCreateRequest request = new IssueMilestoneCreateRequest("테스트 기능 구현");
        String requestJson = objectMapper.writeValueAsString(request);
        willDoNothing().given(issueService).assignMilestone(anyLong(), any(IssueMilestoneCreateRequest.class));

        // when
        ResultActions result = mockMvc.perform(
                post(url).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    @TestFactory
    @DisplayName("이슈 생성 시 요청값에 대한 validation을 진행한다")
    Stream<DynamicTest> create_validation() throws Exception{
        final String url = urlPrefix + "/issues";
        final String memberId = "testMember";
        final String title = "t";
        final String content = "c";

        given(issueService.create(any(IssueCreateRequest.class))).willReturn(1L);

        return Stream.of(
                dynamicTest("제목은 최대 120자 이내여야 한다.", () -> {
                    // given
                    IssueCreateRequest tooLongTitle =
                            new IssueCreateRequest(memberId, title.repeat(120 + 1), content, List.of(),
                                    "테스트 기능 구현");
                    String requestJson = objectMapper.writeValueAsString(tooLongTitle);

                    // when
                    ResultActions result = mockMvc.perform(
                            post(url).content(requestJson)
                                    .contentType(MediaType.APPLICATION_JSON)
                    );

                    // then
                    result.andExpect(status().is4xxClientError());
                }),

                dynamicTest("내용은 최대 2000자 이내여야 한다.", () -> {
                    // given
                    IssueCreateRequest tooLongContent
                            = new IssueCreateRequest(memberId, title, content.repeat(2000 + 1), List.of(),
                            "테스트 기능 구현");
                    String requestJson = objectMapper.writeValueAsString(tooLongContent);

                    // when
                    ResultActions result = mockMvc.perform(
                            post(url).content(requestJson)
                                    .contentType(MediaType.APPLICATION_JSON)
                    );

                    // then
                    result.andExpect(status().is4xxClientError());
                }),

                dynamicTest("마일스톤 아이디를 제외한 모든 필드는 공백이거나 빈 값, null이어선 안 된다.", () -> {
                    // given
                    IssueCreateRequest blankRequest = new IssueCreateRequest(" ", " ", " ",
                            null, null);
                    String requestJson = objectMapper.writeValueAsString(blankRequest);

                    // when
                    ResultActions result = mockMvc.perform(
                            post(url).content(requestJson)
                                    .contentType(MediaType.APPLICATION_JSON)
                    );

                    // then
                    result.andExpect(status().is4xxClientError());
                })
        );
    }

    @Test
    @DisplayName("이슈 id가 1번의 제목을 'Hello update' 로 수정 요청하면 200 OK를 응답한다")
    void editTitle() throws Exception {
        // given
        String url = urlPrefix + "/issues/1";
        String updatedTitle = "Hello update";

        IssueUpdateRequest request = new IssueUpdateRequest(updatedTitle, null);
        String requestJson = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(
                patch(url).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    @DisplayName("이슈의 id를 통해 해당 id issue의 상세 내용을 조회할 수 있다")
    void detail(Long issueId) throws Exception {
        // given
        String url = urlPrefix + "/issues/" + issueId.toString();
        IssueDetailResponse response = IssueDetailResponse.builder()
                .id(issueId)
                .build();
        given(issueService.getDetail(issueId)).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get(url));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("이슈의 id를 통해 해당 id의 issue를 db에서 삭제할 수 있다")
    void deletion() throws Exception {
        // given
        Long issueId = 1L;
        String url = urlPrefix + "/issues/" + issueId;
        willDoNothing().given(issueService).delete(issueId);

        // when
        ResultActions result = mockMvc.perform(
                delete(url)
        );

        // then
        result.andExpect(status().isOk());
    }
}
