package com.issuetracker.domain.label;

import com.issuetracker.domain.label.request.LabelCreateRequest;
import com.issuetracker.domain.label.request.LabelUpdateRequest;
import com.issuetracker.domain.label.response.LabelListResponse;
import com.issuetracker.domain.label.response.LabelResponse;
import com.issuetracker.global.exception.label.LabelDuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public LabelResponse create(LabelCreateRequest labelCreateRequest) {
        labelRepository.findById(labelCreateRequest.getLabelId())
                .ifPresent(label -> {
                    throw new LabelDuplicateException();
                });
        Label label = labelCreateRequest.toEntity();
        Label savedLabel = labelRepository.save(label);
        return LabelResponse.of(savedLabel);
    }

    public LabelListResponse getLabels() {
        List<Label> labels = labelRepository.findAll();
        return LabelListResponse.of(
                labels.stream().map(LabelResponse::of).collect(Collectors.toList())
        );
    }

    public LabelResponse edit(String labelId, LabelUpdateRequest request) {
        if (!Stream.of(request.getLabelId(), request.getDescription(), request.getTextColor(), request.getColorCode())
                .allMatch(Objects::nonNull)) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("labelId", labelId);
        requestMap.put("request", request);

        labelMapper.update(requestMap);
        return LabelResponse.of(request.toEntity());
    }

    public void delete(String labelId) {
        labelRepository.deleteById(labelId);
    }
}
