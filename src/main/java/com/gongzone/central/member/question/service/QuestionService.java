package com.gongzone.central.member.question.service;

import com.gongzone.central.member.question.domain.QuestionMember;
import com.gongzone.central.utils.StatusCode;
import reactor.core.publisher.Mono;

import java.util.List;

public interface QuestionService {

    List<QuestionMember> getAllQuestionMembers();
    void getQuestionStatusUpdate(int memberQuestionNo, StatusCode statusCode);
    void getQuestionMemberInsert(QuestionMember questionMember);
}
