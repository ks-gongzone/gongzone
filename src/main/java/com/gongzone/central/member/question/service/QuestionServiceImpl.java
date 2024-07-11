package com.gongzone.central.member.question.service;

import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import com.gongzone.central.member.alertSSE.service.AlertSEEService;
import com.gongzone.central.member.question.domain.QuestionMember;
import com.gongzone.central.member.question.mapper.QuestionMapper;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;
    private final AlertSEEService alertSEEService;

    @Override
    public List<QuestionMember> getAllQuestionMembers() {
        return questionMapper.findQuestionAll();
    }

    @Override
    public void getQuestionStatusUpdate(int memberQuestionNo, StatusCode statusCode) {
        if (statusCode == StatusCode.S010701 || statusCode == StatusCode.S010702 || statusCode == StatusCode.S010703) {
            questionMapper.updateQuestionStatus(memberQuestionNo, statusCode);
        }
    }

    /*@Override
    public Mono<Void> getQuestionStatusUpdate(int memberQuestionNo, StatusCode statusCode) {
        if (statusCode == StatusCode.S010701 || statusCode == StatusCode.S010702 || statusCode == StatusCode.S010703) {
            return Mono.fromRunnable(() -> questionMapper.updateQuestionStatus(memberQuestionNo, statusCode))
                    .subscribeOn(Schedulers.boundedElastic())
                    .then(sendAlert(memberQuestionNo));
        } else {
            return Mono.empty();
        }
    }*/

    @Override
    public void getQuestionMemberInsert(QuestionMember questionMember) {
        questionMapper.insertQuestion(questionMember);
    }

    /*private Mono<Void> sendAlert(int memberQuestionNo) {
        return Mono.fromCallable(() -> {
                    String memberNo = questionMapper.getMemberNoByQuestionNo(memberQuestionNo);

                    AlertSSE alertSSE = new AlertSSE();
                    alertSSE.setMemberNo(memberNo);
                    alertSSE.setTypeCode("T010204"); // 알림 유형 코드
                    alertSSE.setAlertDetail("테스트.");
                    return alertSSE;
                })
                .flatMap(alertSSE -> {
                    return alertSEEService.sendAlert(alertSSE);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> {
                    e.printStackTrace();
                })
                .doOnSuccess(aVoid -> System.out.println("sendAlert completed successfully"));
    }*/
}
