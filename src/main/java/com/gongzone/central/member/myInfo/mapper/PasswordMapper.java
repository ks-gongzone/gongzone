package com.gongzone.central.member.myInfo.mapper;

import com.gongzone.central.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @제목: 비밀번호 맵핑
 * @생성일: 2024-06-18
 * @수정일: 2024-06-19
 * @내용: member의 기본키 memberNo를 이용해 password 조회 및 업데이트
 */
@Mapper
public interface PasswordMapper {

    Member findByNo(@Param("memberNo") String memberNo);

    void updatePassword(@Param("memberNo") String memberNo, @Param("newPassword") String newPassword);

}
