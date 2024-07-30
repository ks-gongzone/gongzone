package com.gongzone.central.member.alertSSE.service;

import com.gongzone.central.member.alertSSE.domain.AlertAllow;
import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import com.gongzone.central.member.alertSSE.mapper.AlertSSEMapper;
import com.gongzone.central.utils.TypeCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AlertSSEServiceImpl implements AlertSSEService {

    private final AlertSSEMapper alertSSEMapper;
    private final Map<String, Sinks.Many<AlertSSE>> sinks = new ConcurrentHashMap<>();

    @Override
    public Mono<AlertSSE> getAlertSSEBYNo(int alertNo) {
        return Mono.fromCallable(() -> alertSSEMapper.getAlertSSEByNo(alertNo))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AlertSSE> getAlertSSEByMemberNo(String memberNo) {
        return Flux.defer(() -> Flux.fromIterable(alertSSEMapper.alertSSEList(memberNo)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> saveAlertSSE(AlertSSE alertSSE) {
        return Mono.fromRunnable(() -> alertSSEMapper.insertAlertSSE(alertSSE))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> updateReadAlertSSE(int alertNo) {
        return Mono.fromRunnable(() -> alertSSEMapper.updateReadTimeAlertSSE(alertNo))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> updateDeleteAlertSSE(int alertNo) {
        return Mono.fromRunnable(() -> alertSSEMapper .updateDeleteAlertSSE(alertNo))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> sendAlert(AlertSSE alertSSE) {
        System.out.println("알림 실행~~~~~~~");
        System.out.println("alertSSE: " + alertSSE);
        return Mono.fromCallable(() -> alertSSEMapper.getAlertAllow(alertSSE.getMemberNo()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(alertAllow -> {
                    TypeCode alertType = TypeCode.fromCode(alertSSE.getTypeCode());
                    alertAllow.setMemberNo(alertSSE.getMemberNo());
                    if (shouldSendAlert(alertAllow, alertType)) {
                        return Mono.fromCallable(() -> {
                                    alertSSEMapper.insertAlertSSE(alertSSE);
                                    return (Void) null;
                                }).subscribeOn(Schedulers.boundedElastic())
                                .then()
                                .doOnSuccess(unused -> emitAlert(alertSSE));
                    } else {
                        return Mono.error(new RuntimeException("Alert not allowed for type: " + alertType.toString()));
                    }
                });
    }

    private boolean shouldSendAlert(AlertAllow alertAllow, TypeCode alertType) {
        if ("공지".equalsIgnoreCase(alertType.toString())) {
            return true;
        }

        switch (alertType.toString().toLowerCase()) {
            case "sms":
                return alertAllow.isAllowSms();
            case "email":
                return alertAllow.isAllowEmail();
            case "광고성":
                return alertAllow.isAllowMarketing();
            case "회원":
                return alertAllow.isAllowMember();
            case "쪽지":
                return alertAllow.isAllowNote();
            case "게시글":
                return alertAllow.isAllowBulletin();
            case "파티":
                return alertAllow.isAllowParty();
            default:
                return false;
        }
    }

    @Override
    public Mono<List<Map<String, Object>>> countNewAlerts(String memberNo) {
        return Mono.fromCallable(() -> alertSSEMapper.countNewAlerts(memberNo))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AlertSSE> streamAlerts(String memberNo) {
        return sinks.computeIfAbsent(memberNo, k -> Sinks.many().multicast().onBackpressureBuffer())
                .asFlux()
                .doOnCancel(() -> sinks.remove(memberNo));
    }

    @Override
    public void emitAlert(AlertSSE alertSSE) {
        Sinks.Many<AlertSSE> sink = sinks.get(alertSSE.getMemberNo());
        if (sink != null) {
            sink.tryEmitNext(alertSSE);
        }
    }
}
