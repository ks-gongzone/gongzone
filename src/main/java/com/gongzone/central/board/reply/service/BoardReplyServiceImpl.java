package com.gongzone.central.board.reply.service;

import com.gongzone.central.board.domain.BoardReply;
import com.gongzone.central.board.reply.mapper.BoardReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardReplyServiceImpl implements BoardReplyService {
    private final BoardReplyMapper boardReplyMapper;

    @Override
    public void updateReply(BoardReply boardReply) {
        boardReplyMapper.updateReply(boardReply);
    }

    @Override
    public void addReply(BoardReply boardReply) {
        boardReplyMapper.insertReply(boardReply);
    }
}
