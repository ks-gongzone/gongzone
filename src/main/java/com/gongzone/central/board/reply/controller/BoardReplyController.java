package com.gongzone.central.board.reply.controller;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardReply;
import com.gongzone.central.board.reply.service.BoardReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@Slf4j
public class BoardReplyController {
    private final BoardReplyService boardReplyService;

    public BoardReplyController(BoardReplyService boardReplyService) {this.boardReplyService = boardReplyService;}

    @DeleteMapping("/reply/delete")
    public List<BoardReply> deleteReply(@RequestBody BoardReply boardReply) {
        try{
            System.out.println("111111111111111111111111");
            log.info("111111111", boardReply);
            boardReplyService.deleteReply(boardReply);
            List<BoardReply> newReply = boardReplyService.getNewReply(boardReply.getBoardNo());
            return newReply;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/reply/update")
    public List<BoardReply> updateReply(@RequestBody BoardReply boardReply) {
        try{
            boardReplyService.updateReply(boardReply);
            List<BoardReply> newReply = boardReplyService.getNewReply(boardReply.getBoardNo());
            return newReply;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/reply/add")
    public List<BoardReply> addReply(@RequestBody BoardReply boardReply) {
        try{
            boardReplyService.addReply(boardReply);
            List<BoardReply> newReply = boardReplyService.getNewReply(boardReply.getBoardNo());
            return newReply;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
