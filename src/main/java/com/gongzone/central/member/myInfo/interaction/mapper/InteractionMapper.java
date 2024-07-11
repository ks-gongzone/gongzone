package com.gongzone.central.member.myInfo.interaction.mapper;

import com.gongzone.central.member.myInfo.interaction.domain.InteractionMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InteractionMapper {
    // 회원 조회
    InteractionMember findMemberByNo(String memberNo, String currentUserNo);
    // 전체 회원 및 이름 검색
    List<InteractionMember> findAllMembers(
            @Param("currentUserNo") String currentUserNo,
            @Param("memberName") String memberName,
            @Param("offset") int offset,
            @Param("size") int size);
    // 차단 target_no 카운팅
    int getWarningCount(@Param("memberNo") String memberNo);
    // 팔로우 target_no 카운팅
    int getMemberFollowCount(@Param("memberNo") String memberNo);
    // 전체 회원 수 카운팅
    int getTotalMembers(@Param("memberNo") String memberNo);
}
