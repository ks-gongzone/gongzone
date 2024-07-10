package com.gongzone.central.member.question.service;

import com.gongzone.central.member.question.domain.QuestionMember;
import com.gongzone.central.member.question.mapper.QuestionMapper;
import com.gongzone.central.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;

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

    @Override
    public void getQuestionMemberInsert(QuestionMember questionMember) {
        questionMapper.insertQuestion(questionMember);
    }
}
