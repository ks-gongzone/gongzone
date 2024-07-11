package com.gongzone.central.board.reply.service;

import com.gongzone.central.board.domain.BoardReply;

public interface BoardReplyService {
    void updateReply(BoardReply boardReply);
    void addReply(BoardReply boardReply);
}
