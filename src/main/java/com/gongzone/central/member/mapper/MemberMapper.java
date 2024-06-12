package com.gongzone.central.member.mapper;

import com.gongzone.central.member.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberMapper {

    Member findByNumber(String memberNo);

    List<Member> findAll();

    void insert(Member member);

    void update(Member member);

    void delete(Member member);

    Member findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
