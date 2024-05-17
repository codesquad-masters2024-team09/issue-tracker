package com.issuetracker.domain.label;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.issuetracker.domain.label.request.LabelCreateRequest;
import com.issuetracker.domain.label.request.LabelUpdateRequest;
import com.issuetracker.domain.label.response.LabelListResponse;
import com.issuetracker.domain.label.response.LabelResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LabelController.class)
class LabelControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LabelService labelService;

    private final String urlPrefix = "/api/v1";

    @Test
    @DisplayName("레이블을 생성하면 200 상태코드와 레이블의 생성 결과를 응답한다.")
    void create() throws Exception {

        // given
        String url = urlPrefix + "/labels";

        LabelCreateRequest request = new LabelCreateRequest("bug", "버그 라벨", "#FFFFFF", "#000000");
        String requestJson = objectMapper.writeValueAsString(request);
        LabelResponse response = LabelResponse.of(request.toEntity());
        String responseJson = objectMapper.writeValueAsString(response);
        given(labelService.create(any(LabelCreateRequest.class))).willReturn(response);

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

    @Test
    @DisplayName("모든 레이블 목록을 조회할 수 있다.")
    void getLabels() throws Exception {

        // given
        String url = urlPrefix + "/labels";

        String[][] labelData = {
                {"bug", "버그", "#FFFFFF", "#000000"},
                {"feat", "새로운 기능", "#020202", "#EFEFEF"},
                {"fix", "버그 수정", "#757575", "#A0A0A0"}
        };
        List<LabelResponse> labels =
                Arrays.stream(labelData).map(label -> LabelResponse.of(
                        com.issuetracker.domain.label.Label.builder()
                                .id(label[0])
                                .description(label[1])
                                .textColor(label[2])
                                .colorCode(label[3])
                                .build()
        )).toList();
        LabelListResponse labelListResponse = LabelListResponse.of(labels);

        given(labelService.getLabels()).willReturn(labelListResponse);

        // when
        ResultActions result = mockMvc.perform(get(url));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(labelListResponse)));
    }

    @Test
    @DisplayName("레이블 이름으로 조회한 레이블의 이름과 색상코드를 수정하면 수정한 결과가 응답되어야 한다.")
    void edit() throws Exception {
        // given
        String labelId = "bug";
        String url = urlPrefix + "/labels/" + labelId;
        Label labelBeforeUpdate = Label.builder()
                .id(labelId)
                .description("버그 수정")
                .textColor("#FFFFFF")
                .colorCode("#010101")
                .build();

        LabelUpdateRequest request = new LabelUpdateRequest("bugfix", null, null, "#010101");
        LabelResponse response = LabelResponse.of(
                Label.builder()
                        .id(request.getLabelId())
                        .description(labelBeforeUpdate.getDescription())
                        .textColor(labelBeforeUpdate.getTextColor())
                        .colorCode(request.getColorCode())
                        .build()
        );

        given(labelService.edit(any(String.class), any(LabelUpdateRequest.class))).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
                patch(url).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("레이블 이름으로 레이블을 삭제할 수 있다.")
    void deletion() throws Exception{
        // given
        String labelId = "bug";
        String url = urlPrefix + "/labels/" + labelId;
        willDoNothing().given(labelService).delete(labelId);

        // when
        ResultActions result = mockMvc.perform(
                delete(url)
        );

        // then
        result.andExpect(status().isOk());
    }
}