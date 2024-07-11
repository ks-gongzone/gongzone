package com.gongzone.central.board.domain;

import lombok.Data;

import java.util.Date;

@Data
public class BoardReply {
    private int replyNo;
    private String boardNo;
    private String memberNo;
    private String replyBody;
    private Date replyDate;
    private int replyReportCount;
    private String statusCode;
}
