package com.gongzone.central.member.myInfo.alert.controller;

import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.alert.service.AlertService;
import com.gongzone.central.member.myInfo.alert.domain.MyAlert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members/{memberNo}")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;
    private final JwtUtil jwtUtil;

    @GetMapping("/alerts")
    public ResponseEntity<MyAlert> getAlertSettings(@PathVariable String memberNo, Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);
        System.out.println("알람 셋팅 중 memberNo=" + extractedMemberNo);

        if (extractedMemberNo == null || !extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).build();
        }

        MyAlert alert = alertService.getAlertsByMemberNo(memberNo);
        System.out.println("알람 값" + alert);
        if (alert != null) {
            return ResponseEntity.ok(alert);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/alerts/update")
    public ResponseEntity<String> updateAlertSettings(@PathVariable String memberNo, @RequestBody MyAlert myAlert, Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);
        System.out.println("알람 설정 업데이트 중 memberNo=" + extractedMemberNo);
        System.out.println("알람 설정 업데이트 중 myAlert=" + myAlert);

        if (!extractedMemberNo.equals(memberNo) || !myAlert.getMemberNo().equals(memberNo)) {
            return ResponseEntity.status(400).body("회원번호가 일치하지 않습니다.");
        }

        MyAlert alert = alertService.updateAlertSettings(myAlert);
        if(alert == null) {
            return ResponseEntity.status(500).body("알람 설정 업데이트 중 오류 발생");
        }

        return ResponseEntity.ok("알람 설정 업데이트완료");
    }
}
