package com.gongzone.central.member.alertSSE.controller;

import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import com.gongzone.central.member.alertSSE.service.AlertSSEService;
import com.gongzone.central.member.login.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alertSSE")
public class AlertSSEController {

    private final AlertSSEService alertSSEService;
    private final JwtUtil jwtUtil;

    @GetMapping("/getNoteByNo/{alertNo}")
    public Mono<AlertSSE> getNoteByNo(@PathVariable int alertNo) {
        return alertSSEService.getAlertSSEBYNo(alertNo);
    }

    @GetMapping("/AlertSSEList/{memberNo}")
    public Flux<AlertSSE> AlertSSEList(@PathVariable String memberNo) {
        return alertSSEService.getAlertSSEByMemberNo(memberNo);
    }

    @PostMapping("/insertAlertSSE")
    public Mono<Void> insertAlertSSE(@RequestBody AlertSSE alertSSE) {
        return alertSSEService.saveAlertSSE(alertSSE);
    }

    @PostMapping("/updateReadTime/{alertNo}")
    public Mono<Void> updateReadTimeAlertSSE(@PathVariable int alertNo) {
        return alertSSEService.updateReadAlertSSE(alertNo);
    }

    @PostMapping("/updateDelete/{alertNo}")
    public Mono<Void> updateDeleteAlertSSE(@PathVariable int alertNo) {
        return alertSSEService.updateDeleteAlertSSE(alertNo);
    }

    @GetMapping("/AlertSSEListAndCount/{memberNo}")
    public Mono<List<Map<String, Object>>> getAlertListAndCount(@PathVariable String memberNo) {
        return alertSSEService.countNewAlerts(memberNo);
    }

    @GetMapping(value = "/stream/{memberNo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AlertSSE> streamAlerts(@PathVariable String memberNo, @RequestParam String token) {
        if (jwtUtil.validateToken(token)) {
            String tokenMemberNo = jwtUtil.extractMemberNo(token);
            if (memberNo.equals(tokenMemberNo)) {
                return alertSSEService.streamAlerts(memberNo);
            } else {
                return Flux.error(new RuntimeException("Invalid member number"));
            }
        } else {
            return Flux.error(new RuntimeException("Invalid token"));
        }
    }
}
