package com.gongzone.central.member.Management.controller;

import com.gongzone.central.member.Management.domain.*;
import com.gongzone.central.member.Management.service.ManagementService;
import com.gongzone.central.member.domain.Member;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/member/listAll")
    public List<Member> listALl() {
        List<Member> members = managementService.getAllMembers();
        return members;
    }

    @GetMapping("/member/quitListALl")
    public List<MemberQuit> quitListALl() {
        List<MemberQuit> members = managementService.getQuitAllMembers();
        return members;
    }

    @GetMapping("/member/sleepListALl")
    public List<MemberSleep> sleepListALl() {
        List<MemberSleep> members = managementService.getSleepAllMembers();
        return members;
    }

    @GetMapping("/member/punishListALl")
    public List<MemberPunish> punishListALl() {
        List<MemberPunish> members = managementService.getPunishAllMembers();
        return members;
    }

    @PostMapping("/statusUpdate/{memberNo}")
    public ResponseEntity<String> statusUpdate(@PathVariable String memberNo, @RequestBody ManagementRequest request) {
        System.out.println("11111111111111111111111111");
        StatusCode statusCode = StatusCode.fromCode(request.getStatusCode());
        managementService.getStatusUpdate(memberNo, statusCode);
        return ResponseEntity.ok(String.valueOf(statusCode));
    }

    @PostMapping("/punish/update/{memberNo}")
    public ResponseEntity<Boolean> punishUpdate(@PathVariable String memberNo, @RequestBody ResponseAdminMember responseAdminMember) {
        System.out.println("1111111111111111111111111");
        MemberPunish memberPunish = MemberPunish.builder()
                        .memberNo(memberNo)
                        .punishType(responseAdminMember.getTypeCode())
                        .punishReason(responseAdminMember.getReasonDetail())
                        .punishPeriod(responseAdminMember.getPeriod())
                        .build();

        try {
            managementService.getPeriodupdate(memberPunish);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            System.out.println("실패 : " + e.getMessage());
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/punish/insert/{memberNo}")
    public ResponseEntity<Boolean> punishInsert(@PathVariable String memberNo, @RequestBody ResponseInsert responseInsert) {
        MemberPunish memberPunish = MemberPunish.builder()
                .memberNo(memberNo)
                .memberAdminNo(responseInsert.getMemberAdminNo())
                .punishType(responseInsert.getTypeCode())
                .punishReason(responseInsert.getReasonDetail())
                .punishPeriod(responseInsert.getPeriod())
                .punishStatus(responseInsert.getStatusCode())
                .memberStatus(responseInsert.getMemberStatusCode())
                .build();

        try {
            managementService.getPunishInsert(memberPunish);

            StatusCode memberStatusCode = StatusCode.fromCode(responseInsert.getMemberStatusCode());
            managementService.getStatusUpdate(memberNo, memberStatusCode);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            System.out.println("실패 : " + e.getMessage());
            return ResponseEntity.ok(false);
        }
    }
}
