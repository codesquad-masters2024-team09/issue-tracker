package com.issuetracker.domain.milestone;

import com.issuetracker.domain.common.BaseDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Table("MILESTONE")
public class Milestone extends BaseDateTime implements Persistable<String> {

    @Id
    @Column("MILESTONE_ID")
    private String id;

    @Builder.Default
    private boolean isOpen = true;
    private LocalDateTime dueDate;
    private String description;

    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
