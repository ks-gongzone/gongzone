package com.gongzone.central.member.myInfo.alert.controller;

import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import com.gongzone.central.member.myInfo.alert.domain.MyAlert;
import com.gongzone.central.member.myInfo.alert.service.AlertService;
import com.gongzone.central.member.myInfo.service.MyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;
    private final MyInfoService myInfoService; // 알람 설정시 memberNo일 경우 대처
    private final JwtUtil jwtUtil;

    @GetMapping("/{memberNo}/alerts")
    public ResponseEntity<Map<String, Object>> getAlertSettings(@PathVariable String memberNo, Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (extractedMemberNo == null || !extractedMemberNo.equals(memberNo)) {
            return ResponseEntity.status(403).build();
        }

        MyAlert alert = alertService.getAlertsByMemberNo(memberNo);
        Map<String, Object> response = new HashMap<>();

        if (alert != null) {
            response.put("alertData", alert);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "데이터가 없습니다.");
            return ResponseEntity.status(404).body(response);
        }
    }

    @PostMapping("/{memberNo}/alerts/update")
    public ResponseEntity<Map<String, String>> updateAlertSettings(@PathVariable String memberNo, @RequestBody Map<String, Object> alertData, Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "회원번호 불일치");
            return ResponseEntity.status(400).body(response);
        }

        MyAlert myAlert = new MyAlert().useMyAlert(memberNo, alertData);
        alertService.setDefaultAlertValues(myAlert); // 기본값 설정
        try {
            alertService.updateAlertSettings(myAlert);
            Map<String, String> response = new HashMap<>();
            response.put("message", "알람 설정 업데이트 완료");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            if (e.getMessage().contains("데이터가 없습니다.")) {
                response.put("message", "기존 데이터가 없습니다.");
                return ResponseEntity.status(500).body(response);
            } else {
                response.put("message", "알림 설정 업데이트 중 오류 발생");
                return ResponseEntity.status(500).body(response);
            }
        }
    }

    @PostMapping("/{memberNo}/alerts/insert")
    public ResponseEntity<Map<String, String>> insertAlertSettings(@PathVariable String memberNo, @RequestBody Map<String, Object> alertData, Authentication authentication) {
        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String extractedMemberNo = jwtUtil.extractMemberNo(token);

        if (!extractedMemberNo.equals(memberNo)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "회원번호가 일치하지 않습니다.");
            return ResponseEntity.status(400).body(response);
        }

        MyAlert myAlert = new MyAlert().useMyAlert(memberNo, alertData);
        alertService.setDefaultAlertValues(myAlert); // 기본값 설정

        try {
            alertService.insertAlertSettings(myAlert);
            Map<String, String> response = new HashMap<>();
            response.put("message", "알림 설정이 생성되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "알림 설정 생성 중 오류 발생");
            return ResponseEntity.status(500).body(response);
        }
    }
}