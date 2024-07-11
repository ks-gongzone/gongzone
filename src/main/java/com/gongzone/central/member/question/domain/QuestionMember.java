package com.gongzone.central.member.question.domain;

import lombok.Data;

import java.util.Date;

@Data
public class QuestionMember {
    private int memberQuestionNo;
    private String memberNo;
    private String typeCode;
    private String memberQuestionBody;
    private Date memberQuestionDate;
    private String statusCode;
}
