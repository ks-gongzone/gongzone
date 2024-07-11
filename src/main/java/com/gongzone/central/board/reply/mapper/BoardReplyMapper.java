package com.gongzone.central.board.reply.mapper;

import com.gongzone.central.board.domain.BoardReply;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardReplyMapper {
    void updateReply(BoardReply boardReply);
    void insertReply(BoardReply boardReply);
}
