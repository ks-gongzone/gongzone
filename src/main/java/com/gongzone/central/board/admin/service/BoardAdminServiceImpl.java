package com.gongzone.central.board.admin.service;

import com.gongzone.central.board.admin.domain.BoardAdminInfo;
import com.gongzone.central.board.admin.domain.BoardProgress;
import com.gongzone.central.board.admin.domain.BoardWriteDate;
import com.gongzone.central.board.admin.domain.BoardWriteMember;
import com.gongzone.central.board.admin.mapper.BoardAdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardAdminServiceImpl implements BoardAdminService {
    private final BoardAdminMapper boardAdminMapper;

    @Override
    public BoardAdminInfo getBoardAdminInfo() {
        BoardAdminInfo boardAdminInfo = new BoardAdminInfo();

        List<BoardProgress> boardProgress = boardAdminMapper.getBoardProgress();
        List<BoardWriteMember> boardWriteMember = boardAdminMapper.getBoardWriteMember();
        List<BoardWriteDate> boardWriteDate = boardAdminMapper.getBoardWriteDate();

        boardAdminInfo.setBoardProgressList(boardProgress);
        boardAdminInfo.setBoardWriteMemberList(boardWriteMember);
        boardAdminInfo.setBoardWriteDateList(boardWriteDate);

        return boardAdminInfo;
    }
}
