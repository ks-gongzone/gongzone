package com.gongzone.central.board.domain;

import lombok.Data;

@Data
public class BoardSearchRequest {
    private String location;
    private String category;
    private String content;
    private String memberNo;
}
