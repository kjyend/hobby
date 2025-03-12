package com.hobbyproject.repository.member;

import com.hobbyproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberRepository extends JpaRepository<Member, String> {
    @Query("SELECT m.name FROM Member m WHERE m.loginId = :id")
    String findMemberName(@Param("id") String loginId);

    Member findMemberByLoginId(String loginId);
}
