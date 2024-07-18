package com.gongzone.central.board.admin.domain;

import lombok.Data;

import java.util.List;

@Data
public class BoardAdminInfo {
    private List<BoardProgress> boardProgressList;
    private List<BoardWriteMember> boardWriteMemberList;
    private List<BoardWriteDate> boardWriteDateList;
}
