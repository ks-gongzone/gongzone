package com.gongzone.central.member.question.controller;

import com.gongzone.central.member.question.domain.QuestionMember;
import com.gongzone.central.member.question.service.QuestionService;
import com.gongzone.central.member.report.domain.ReportMember;
import com.gongzone.central.member.report.domain.RequestReportMember;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/QuestionMember/listQuestionAll")
    public List<QuestionMember> listQuestionALl() {
        List<QuestionMember> questionMember = questionService.getAllQuestionMembers();
        return questionMember;
    }

    @PostMapping("/QuestionStatusUpdate/{memberQuestionNo}")
    public String statusUpdate(@PathVariable int memberQuestionNo, @RequestBody QuestionMember questionMember) {
        StatusCode statusCode = StatusCode.fromCode(questionMember.getStatusCode());
        questionService.getQuestionStatusUpdate(memberQuestionNo, statusCode);
        return statusCode.toString();
    }

    @PostMapping("/QuestionMember/insert")
    public ResponseEntity<Boolean> questionMemberInsert(@RequestBody QuestionMember questionMember) {
        try {
            questionService.getQuestionMemberInsert(questionMember);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}
