package com.gongzone.central.board.reply.service;

import com.gongzone.central.board.domain.BoardReply;
import com.gongzone.central.board.mapper.BoardMapper;
import com.gongzone.central.board.reply.mapper.BoardReplyMapper;
import com.gongzone.central.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardReplyServiceImpl implements BoardReplyService {
    private final BoardReplyMapper boardReplyMapper;
    private final BoardMapper boardMapper;
    private final MemberMapper memberMapper;

    @Override
    public void deleteReply(BoardReply boardReply) {
        boardReplyMapper.deleteReply(boardReply);
    }

    @Override
    public List<BoardReply> getNewReply(String boardNo){
        List<BoardReply> newReply = boardMapper.getBoardReplyList(boardNo);
        for (int i=0; i<newReply.size(); i++) {
            String memberId = (memberMapper.info(newReply.get(i).getMemberNo())).getMemberId();
            newReply.get(i).setMemberId(memberId);
        }
        return newReply;
    }

    @Override
    public void updateReply(BoardReply boardReply) {
        boardReplyMapper.updateReply(boardReply);
    }

    @Override
    public void addReply(BoardReply boardReply) {
        boardReplyMapper.insertReply(boardReply);
    }
}
