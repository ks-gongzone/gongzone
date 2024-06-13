package com.gongzone.central.member.mapper;

import com.gongzone.central.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberMapper {

    void insert(Member member);
    List<Member> findAll();
    Member findByNo(String memberNo);
    void update(Member member);
    void delete(String member);

    @Select("SELECT COALESCE(MAX(CAST(SUBSTRING(m_no, 2) AS UNSIGNED)), 0) FROM member")
    int findMemberNo();
}
