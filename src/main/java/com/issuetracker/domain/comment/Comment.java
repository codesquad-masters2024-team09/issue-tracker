package com.issuetracker.domain.comment;

import com.issuetracker.domain.common.BaseDateTime;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Table("COMMENT")
public class Comment extends BaseDateTime {

    @Id
    @Column("COMMENT_ID")
    private Long id;
    private String memberId;
    private Long issueId;
    private String content;

    public void initBaseDateTime() {
        LocalDateTime now = LocalDateTime.now();
        setCreatedAt(now);
        setModifiedAt(now);
    }
}
