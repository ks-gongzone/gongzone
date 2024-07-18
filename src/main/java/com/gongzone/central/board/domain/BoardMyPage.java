package com.gongzone.central.board.domain;

import lombok.Data;

@Data
public class BoardMyPage {
    private String boardTitle;
    private String boardBody;
    private String partyNo;
    private String memberNo;
    private int partyAmount;
    private String statusCode;
    private String boardNo;
    private String filePath;
}
