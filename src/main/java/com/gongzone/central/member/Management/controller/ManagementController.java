package com.gongzone.central.member.Management.controller;

import com.gongzone.central.member.Management.domain.ManagementRequest;
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
    public List<Member> quitListALl() {
        System.out.println("1111111111111111111111111");
        List<Member> members = managementService.getQuitAllMembers();
        System.out.println("members : " + members.size());
        return members;
    }

    @GetMapping("/member/sleepListALl")
    public List<Member> sleepListALl() {
        System.out.println("1111111111111111111111111");
        List<Member> members = managementService.getSleepAllMembers();
        System.out.println("members : " + members.size());
        return members;
    }

    @GetMapping("/member/punishListALl")
    public List<Member> punishListALl() {
        System.out.println("1111111111111111111111111");
        List<Member> members = managementService.getPunishAllMembers();
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
}
