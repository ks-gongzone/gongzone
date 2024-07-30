package com.gongzone.central.member.question.service;

import com.gongzone.central.member.alertSSE.service.AlertSSEService;
import com.gongzone.central.member.question.domain.QuestionMember;
import com.gongzone.central.member.question.mapper.QuestionMapper;
import com.gongzone.central.utils.StatusCode;
import com.gongzone.central.utils.TypeCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;
    private final AlertSSEService alertSEEService;

    @Override
    public List<QuestionMember> getAllQuestionMembers() {
        List<QuestionMember> questionMembers = questionMapper.findQuestionAll();
        questionMembers.forEach(questionMember -> {
            questionMember.setStatusCode(StatusCode.getDescriptionByCode(questionMember.getStatusCode()));
            questionMember.setTypeCode(TypeCode.getDescriptionByCode(questionMember.getTypeCode()));
        });
        return questionMembers;
    }

    @Override
    public void getQuestionStatusUpdate(int memberQuestionNo, StatusCode statusCode) {
        if (statusCode == StatusCode.S010701 || statusCode == StatusCode.S010702 || statusCode == StatusCode.S010703) {
            questionMapper.updateQuestionStatus(memberQuestionNo, statusCode);
        }
    }

    @Override
    public void getQuestionMemberInsert(QuestionMember questionMember) {
        questionMapper.insertQuestion(questionMember);
    }
}
