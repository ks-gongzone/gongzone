package com.gongzone.central.member.alertSSE.controller;

import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import com.gongzone.central.member.alertSSE.service.AlertSEEService;
import com.gongzone.central.member.note.domain.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlertSEEController {

    private final AlertSEEService alertSEEService;

    @GetMapping("/getNoteByNo/{alertNo}")
    public Mono<AlertSSE> getNoteByNo(@PathVariable int alertNo) {
        return alertSEEService.getAlertSSEBYNo(alertNo);
    }

    @GetMapping("/AlertSSEList/{memberNo}")
    public Flux<AlertSSE> AlertSSEList(@PathVariable String memberNo) {
        return alertSEEService.getAlertSSEByMemberNo(memberNo);
    }

    @PostMapping("/insertAlertSSE")
    public Mono<Void> insertAlertSSE(@RequestBody AlertSSE alertSSE) {
        return alertSEEService.saveAlertSSE(alertSSE);
    }

    @PostMapping("/updateReadTime/{alertNo}")
    public Mono<Void> updateReadTimeAlertSSE(@PathVariable int alertNo) {
        return alertSEEService.updateReadAlertSSE(alertNo);
    }

    @PostMapping("/updateDelete/{alertNo}")
    public Mono<Void> updateDeleteAlertSSE(@PathVariable int alertNo) {
        return alertSEEService.updateDeleteAlertSSE(alertNo);
    }
}
