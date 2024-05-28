package com.issuetracker.domain.label.request;

import com.issuetracker.domain.label.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LabelUpdateRequest {

    private String labelId;
    private String description;
    private String textColor;
    private String colorCode;

    public Label toEntity() {
        return Label.builder()
                .id(labelId)
                .description(description)
                .textColor(textColor)
                .colorCode(colorCode)
                .build();
    }

    public boolean validateNullOrBlank() {
        return !((labelId == null || labelId.isBlank())
                && (textColor == null || textColor.isBlank())
                && (colorCode == null || colorCode.isBlank()));
    }
}
