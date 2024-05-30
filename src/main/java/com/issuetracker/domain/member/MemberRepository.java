package com.issuetracker.domain.member;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends CrudRepository<Member, String> {

    @Modifying
    @Query("UPDATE MEMBER SET REFRESH_TOKEN = :refreshToken WHERE MEMBER_ID = :memberId")
    void updateRefreshToken(String memberId, String refreshToken);

    List<Member> findAll();
}
