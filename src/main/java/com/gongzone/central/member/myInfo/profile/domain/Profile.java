package com.gongzone.central.member.myInfo.profile.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongzone.central.file.domain.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {
    // 회원정보
    private final String memberNo; // 로그인한 유저
    private final String memberName;
    private final char gender;
    private final int follower; // follow테이블의 로그인한 유저 follow m_target_no count
    private final int following; // follow테이블의 로그인한 유저 m_no숫자 count
    // 작성글은 board테이블에 b_no가 기본키, m_no가 작성자이므로 m_no의 카운팅만 해서 보여주면 된다.
    private final int boardCount; // 작성 글 수

    // 사진 사용처
    private int fileRelationNo;  // 파일 관계 고유 번호
    private int fileNo;          // 파일 고유 번호
    private String fileUsage;        // 파일 사용처

    // 사진 정보
    private List<FileUpload> files;

}
