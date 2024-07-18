package com.gongzone.central.member.question.mapper;

import com.gongzone.central.member.question.domain.QuestionMember;
import com.gongzone.central.utils.StatusCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {
    List<QuestionMember> findQuestionAll();

    void updateQuestionStatus(int memberQuestionNo, StatusCode statusCode);

    void insertQuestion(QuestionMember questionMember);

    String getMemberNoByQuestionNo(int memberQuestionNo);
}
