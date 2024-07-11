package com.gongzone.central.member.myInfo.dropdown.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @제목: 드롭박스 설정
 * @생성일: 2024-07-10
 * @수정일: 2024-07-10
 * @내용: 드롭다운 박스 값 조회
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(force= true)
@Builder
public class DropDownInfo {
    private final String memberNo;
    private final String memberName;
    private final String pointNo;
    private final int memberPoint;
}
