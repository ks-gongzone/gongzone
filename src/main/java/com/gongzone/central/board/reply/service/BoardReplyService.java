package com.gongzone.central.board.reply.service;

import com.gongzone.central.board.domain.BoardReply;

import java.util.List;

public interface BoardReplyService {
    List<BoardReply> getNewReply(String boardNo);
    void updateReply(BoardReply boardReply);
    void addReply(BoardReply boardReply);
}
