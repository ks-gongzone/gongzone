package com.gongzone.central.member.mapper;

import com.gongzone.central.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    void insert(Member member);
    List<Member> findAll();
    Member findByNo(String memberNo);
    Boolean findById();
    void update(Member member);
    void delete(String member);

//    int findMemberNo();
}
