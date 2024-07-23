package com.gongzone.central.member.alertSSE.service;

import com.gongzone.central.member.alertSSE.domain.AlertAllow;
import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface AlertSEEService {

    Mono<AlertSSE> getAlertSSEBYNo(int alertNo);
    Flux<AlertSSE> getAlertSSEByMemberNo(String memberNo);
    Mono<Void> saveAlertSSE(AlertSSE alertSSE);
    Mono<Void> updateReadAlertSSE(int alertNo);
    Mono<Void> updateDeleteAlertSSE(int alertNo);
    Mono<Void> sendAlert(AlertSSE alertSSE);
    Mono<List<Map<String, Object>>> countNewAlerts(String memberNo);
}