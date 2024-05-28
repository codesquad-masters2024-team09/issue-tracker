package com.issuetracker.domain.milestone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.domain.milestone.request.MilestoneCreateRequest;
import com.issuetracker.domain.milestone.request.MilestoneUpdateRequest;
import com.issuetracker.domain.milestone.response.MilestoneListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MilestoneController.class)
class MilestoneControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MilestoneService milestoneService;

    private final String urlPrefix = "/api/v1";

    @Test
    @DisplayName("마일스톤을 생성하면 200 상태코드와 마일스톤 아이디를 반환한다")
    void create() throws Exception {
        // given
        String url = urlPrefix + "/milestones";

        MilestoneCreateRequest request = new MilestoneCreateRequest(
                "testName", LocalDate.of(2024, 5, 24), "testDescription");
        String requestJson = objectMapper.writeValueAsString(request);
        given(milestoneService.create(any(MilestoneCreateRequest.class))).willReturn(request.getId());
        String responseJson = objectMapper.writeValueAsString(Collections.singletonMap("milestoneId", request.getId()));

        // when
        ResultActions result = mockMvc.perform(
                post(url).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson));
    }

    @TestFactory
    @DisplayName("마일스톤 생성 시 요청값에 대한 validation을 진행한다")
    Stream<DynamicTest> create_validation() throws Exception {
        final String url = urlPrefix + "/milestones";
        final String id = "n";
        final String description = "d";
        final LocalDate dueDate = LocalDate.of(2024, 5, 24);

        given(milestoneService.create(any(MilestoneCreateRequest.class))).willReturn(id);

        return Stream.of(
                dynamicTest("마일스톤 이름은 최대 30자 이내여야 한다", () -> {
                    // given
                    MilestoneCreateRequest tooLongTitle = new MilestoneCreateRequest(id.repeat(30+1), dueDate, description);
                    String requestJson = objectMapper.writeValueAsString(tooLongTitle);

                    // when
                    ResultActions result = mockMvc.perform(
                            post(url).content(requestJson)
                                    .contentType(MediaType.APPLICATION_JSON)
                    );

                    // then
                    result.andExpect(status().is4xxClientError());
                }),

                dynamicTest("마일스톤의 설명은 최대 50자여야 한다", () -> {
                    // given
                    MilestoneCreateRequest tooLongDescription = new MilestoneCreateRequest(id, dueDate, description.repeat(50+1));
                    String requestJson = objectMapper.writeValueAsString(tooLongDescription);

                    // when
                    ResultActions result = mockMvc.perform(
                            post(url).content(requestJson)
                                    .contentType(MediaType.APPLICATION_JSON)
                    );

                    // then
                    result.andExpect(status().is4xxClientError());
                }),

                dynamicTest("모든 필드는 공백이거나 빈 값, null이어선 안 된다.", () -> {
                    // given
                    MilestoneCreateRequest blankRequest = new MilestoneCreateRequest(" ",  null, " ");
                    String requestJson = objectMapper.writeValueAsString(blankRequest);

                    // when
                    ResultActions result = mockMvc.perform(
                            post(url).content(requestJson)
                                    .contentType(MediaType.APPLICATION_JSON)
                    );

                    // then
                    result.andExpect(status().is4xxClientError());
                }));
    }


    @Test
    @DisplayName("마일스톤 목록을 상태별로 조회할 수 있다")
    void getMilestones() throws Exception {
        // given
        String urlForOpened = urlPrefix + "/milestones?isOpen=true";
        String urlForClosed = urlPrefix + "/milestones?isOpen=false";

        MilestoneListResponse openMilestones = MilestoneListResponse.of(
                List.of(MilestoneDetails.builder()
                        .isOpen(true)
                        .id("openMilestone")
                        .totalIssues(10)
                        .openIssues(5)
                        .build())
        );
        MilestoneListResponse closedMilestones = MilestoneListResponse.of(
                List.of(MilestoneDetails.builder()
                        .isOpen(false)
                        .id("closedMilestone")
                        .totalIssues(10)
                        .openIssues(5)
                        .build())
        );

        String responseJsonOfOpened = objectMapper.writeValueAsString(openMilestones);
        String responseJsonOfClosed = objectMapper.writeValueAsString(closedMilestones);

        given(milestoneService.getMilestones(true)).willReturn(openMilestones);
        given(milestoneService.getMilestones(false)).willReturn(closedMilestones);

        // when & then
        performRequestAndVerify(urlForOpened, responseJsonOfOpened);
        performRequestAndVerify(urlForClosed, responseJsonOfClosed);
    }

    private void performRequestAndVerify(String url, String expectedJson) throws Exception {
        ResultActions result = mockMvc.perform(
                get(url).contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    @DisplayName("id가 1인 마일스톤의 제목을 수정하면 200 OK를 반환한다")
    void editTitle() throws Exception {
        // given
        String url = urlPrefix + "/milestones/testMilestone";
        String updatedName = "Milestone update";

        MilestoneUpdateRequest request = new MilestoneUpdateRequest(
                updatedName, null, null
        );

        String requestJson = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(
                put(url).content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("마일스톤의 id를 통해 해당 id의 마일스톤에 대한 삭제를 요청받아 성공할 경우 200 OK를 반환한다")
    void deletion() throws Exception {
        // given
        String milestoneId = "test milestone";
        String url = urlPrefix + "/milestones/" + milestoneId;
        willDoNothing().given(milestoneService).delete(milestoneId);

        // when
        ResultActions result = mockMvc.perform(
                delete(url)
        );

        // then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("열린 마일스톤 수를 조회할 수 있다")
    void getMilestoneCount() throws Exception {
        // given
        boolean openStatus = true;
        Long countResult = 5L;
        String url = urlPrefix + "/milestones/count?isOpen=" + openStatus;
        given(milestoneService.count(openStatus)).willReturn(countResult);

        // when
        ResultActions result = mockMvc.perform(
                get(url).contentType(MediaType.APPLICATION_JSON)
        );

        // then
       result.andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonMap("countResult", countResult))));
    }

    @Test
    @DisplayName("마일스톤 상태를 업데이트 요청에 성공하면 200 OK를 반환한다")
    void updateStatus() throws Exception {
        // given
        String milestoneId = "test milestone";
        boolean isOpen = false;
        String url = urlPrefix + "/milestones/status?milestoneId=" + milestoneId + "&isOpen=" + isOpen;
        doNothing().when(milestoneService).updateStatus(milestoneId, isOpen);

        // when
        ResultActions result = mockMvc.perform(
                patch(url).contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }
}
