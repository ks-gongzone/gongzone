package com.gongzone.central.member.myInfo.dropdown.controller;

import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.dropdown.domain.DropDownInfo;
import com.gongzone.central.member.myInfo.dropdown.service.DropDownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/dropDown")
public class DropDownController {
    private final JwtUtil jwtUtil;
    private final DropDownService dropDownService;

    /**
     * @제목: 특정 회원 드롭다운 데이터 조회
     * @수정일: 2024-07-10
     * @내용: 로그인된 특정 회원의 드롭다운 데이터 반환
     */
    @GetMapping("/{memberNo}")
    public ResponseEntity<DropDownInfo> getDropDownData(
            @PathVariable String memberNo,
            Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);
        String extractedPointNo = jwtUtil.extractPointNo(token);

        System.out.println("[컨트롤러] 드롭다운 데이터 조회" + memberNo);

        if (!extractedMemberNo.equals(memberNo)) {
            System.out.println("[컨트롤러] 로그인이 필요합니다.");
            return ResponseEntity.status(403).build();
        }

        DropDownInfo memberData = dropDownService.findByData(memberNo, extractedPointNo);
        if(memberData != null) {
            DropDownInfo dropDownInfo = DropDownInfo.builder()
                    .memberNo(extractedMemberNo)
                    .memberName(memberData.getMemberName())
                    .pointNo(extractedPointNo)
                    .memberPoint(memberData.getMemberPoint())
                    .build();
            return ResponseEntity.ok(dropDownInfo);
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}
