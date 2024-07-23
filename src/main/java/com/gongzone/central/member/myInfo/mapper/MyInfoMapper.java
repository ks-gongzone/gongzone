package com.gongzone.central.member.myInfo.mapper;

import com.gongzone.central.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @제목: 비밀번호 맵핑
 * @생성일: 2024-06-18
 * @수정일: 2024-07-02
 * @내용: 닉네임 업데이트 시 받아오는 데이터 형태에 맞춰 타입 변경
 */
@Mapper
public interface MyInfoMapper {

    Member findByNo(@Param("memberNo") String memberNo);
    Member findByNickname(@Param("memberNick") String memberNick);
    Member findByAddress(@Param("memberAddress") String memberAddress);
    Member findByPhone(@Param("memberNo") String memberNo);

    void updatePassword(@Param("memberNo") String memberNo, @Param("newPassword") String newPassword);
    void updateMemberNick(Map<String, Object> params);
    void updateMemberAddress(@Param("memberNo") String memberNo, @Param("newMemberAddress") String newMemberAddress);
    void updateStatusCode(Map<String, Object> params);
}
