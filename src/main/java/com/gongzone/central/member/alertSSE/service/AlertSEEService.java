package com.gongzone.central.member.alertSSE.service;

import com.gongzone.central.member.alertSSE.domain.AlertAllow;
import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AlertSEEService {

    Mono<AlertSSE> getAlertSSEBYNo(int alertNo);
    Flux<AlertSSE> getAlertSSEByMemberNo(String memberNo);
    Mono<Void> saveAlertSSE(AlertSSE alertSSE);
    Mono<Void> updateReadAlertSSE(int alertNo);
    Mono<Void> updateDeleteAlertSSE(int alertNo);
    Mono<Void> sendAlert(AlertSSE alertSSE);
}


/*AlertSSE getAlertSSEByNo(String memberNo);
List<AlertSSE> alertSSEList(String memberNo);
void insertAlertSSE(AlertSSE alertSSE);
void updateReadTimeAlertSSE(int noteNo);
void updateDeleteAlertSSE(int noteNo);
AlertAllow getAlertAllow(String memberNo);*/
