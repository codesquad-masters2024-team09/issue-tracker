package com.issuetracker.domain.member;

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
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Table("MEMBER")
public class Member extends BaseDateTime implements Persistable<String> {

    @Id
    @Column("MEMBER_ID")
    private String id;

    @Column("IMG_URL")
    private String profileImgUrl;

    @Column("PASSWORD")
    private String encodedPassword;
    private String refreshToken;

    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void updateRefreshToken(String token) {
        this.refreshToken = token;
    }

    public void expireRefreshToken() {
        updateRefreshToken(null);
    }
}
