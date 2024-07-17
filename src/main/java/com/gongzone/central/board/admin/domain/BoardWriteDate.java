package com.gongzone.central.board.admin.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BoardWriteDate {
    private LocalDate writeDate;
    private int boardCount;
}
