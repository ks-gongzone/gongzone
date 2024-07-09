package com.gongzone.central.announce.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Announce {
    private int announceNo; // DB에서 기본키에 해당
    private String memberNo; // 관리자인 memberNo1인 유저만 글 쓰기 가능하나 조회는 누구나 가능
    private String typeCode; // 공지, 프로모션, FAQ등 분류 기능
    private String announceTitle; // 썸네일에선 제목까지만 표기
    private String announceBody; // 현재 글이 길어지면 String 말고 다른 형태로 받아야할지 고민
    private Date announceDate; // 날짜는 다른 형태로 받을 수 있다면 받아오는걸로
    private int viewCount; // 로직을 통해 글 접속 시 숫자 한개씩 증가하게 할 예정
    private String typeCodeDes; // 상태코드 상세 설명
}
