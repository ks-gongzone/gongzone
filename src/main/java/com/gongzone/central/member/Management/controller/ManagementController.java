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
        System.out.println("1111111111111111111111111");
        List<Member> members = managementService.getAllMembers();
        System.out.println("members : " + members.size());
        return members;
    }

    @GetMapping("/member/quitListALl")
    public List<MemberQuit> quitListALl() {
        System.out.println("1111111111111111111111111");
        List<MemberQuit> members = managementService.getQuitAllMembers();
        System.out.println("members : " + members.size());
        return members;
    }

    @GetMapping("/member/sleepListALl")
    public List<MemberSleep> sleepListALl() {
        System.out.println("1111111111111111111111111");
        List<MemberSleep> members = managementService.getSleepAllMembers();
        System.out.println("members : " + members.size());
        return members;
    }

    @GetMapping("/member/punishListALl")
    public List<MemberPunish> punishListALl() {
        System.out.println("1111111111111111111111111");
        List<MemberPunish> members = managementService.getPunishAllMembers();
        System.out.println("members : " + members.size());
        return members;
    }

    @PostMapping("/statusUpdate/{memberNo}")
    public ResponseEntity<String> statusUpdate(@PathVariable String memberNo, @RequestBody ManagementRequest request) {
        System.out.println("11111111111111111111111111");
        StatusCode statusCode = StatusCode.fromCode(request.getStatusCode());
        managementService.getStatusUpdate(memberNo, statusCode);
        System.out.println("statusCode : " + statusCode);
        System.out.println("memberNo : " + memberNo);
        return ResponseEntity.ok(String.valueOf(statusCode));
    }

    @PostMapping("/punish/update/{memberNo}")
    public ResponseEntity<String> punishUpdate(@PathVariable String memberNo, @RequestBody ResponseAdminMember responseAdminMember) {
        System.out.println("1111111111111111111111111");
        MemberPunish memberPunish = MemberPunish.builder()
                        .memberNo(memberNo)
                        .punishType(responseAdminMember.getTypeCode())
                        .punishReason(responseAdminMember.getReasonDetail())
                        .punishPeriod(responseAdminMember.getPeriod())
                        .build();

        System.out.println("memberPunish : " + memberPunish);
        System.out.println("memberNo : " + memberNo);
        System.out.println("reasonDetail : " + responseAdminMember.getReasonDetail());
        System.out.println("period : " + responseAdminMember.getPeriod());
        System.out.println("typeCode : " + responseAdminMember.getTypeCode());

        managementService.getPeriodupdate(memberPunish);
        return ResponseEntity.ok(String.valueOf(memberPunish));
    }

    @PostMapping("/punish/insert/{memberNo}")
    public ResponseEntity<String> punishInsert(@PathVariable String memberNo, @RequestBody ResponseInsert responseInsert) {
        System.out.println("1111111111111111111111111");
        MemberPunish memberPunish = MemberPunish.builder()
                .memberNo(memberNo)
                .memberAdminNo(responseInsert.getMemberAdminNo())
                .punishType(responseInsert.getTypeCode())
                .punishReason(responseInsert.getPunishReason())
                .punishPeriod(responseInsert.getPunishPeriod())
                .punishStatus(responseInsert.getStatusCode())
                .build();

        System.out.println("memberPunish : " + memberPunish);
        System.out.println("memberNo : " + memberNo);

        managementService.getPunishInsert(memberPunish);

        StatusCode memberStatusCode = StatusCode.fromCode(responseInsert.getMemberStatusCode());
        managementService.getStatusUpdate(memberNo, memberStatusCode);

        return ResponseEntity.ok(String.valueOf(memberPunish));
    }
}
