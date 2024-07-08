package com.gongzone.central.member.Management.controller;

import com.gongzone.central.member.Management.domain.ManagementRequest;
import com.gongzone.central.member.Management.service.ManagementService;
import com.gongzone.central.member.domain.Member;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/listALl")
    public ResponseEntity<List<Member>> listALl() {
        System.out.println("1111111111111111111111111");
        List<Member> members = managementService.getAllMembers();
        return ResponseEntity.ok(members);
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
