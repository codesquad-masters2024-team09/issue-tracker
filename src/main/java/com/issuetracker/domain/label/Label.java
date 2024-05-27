package com.issuetracker.domain.label;

import com.issuetracker.domain.common.BaseDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Table("LABEL")
public class Label extends BaseDateTime implements Persistable<String> {

    @Id
    @Column("LABEL_ID")
    private String id;
    private String description;
    private String textColor;
    private String colorCode;

    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
