package com.gongzone.central.board.reply.controller;

import com.gongzone.central.board.domain.BoardReply;
import com.gongzone.central.board.reply.service.BoardReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@Slf4j
public class BoardReplyController {
    private final BoardReplyService boardReplyService;

    public BoardReplyController(BoardReplyService boardReplyService) {this.boardReplyService = boardReplyService;}

    @PostMapping("/reply/update")
    public ResponseEntity<String> updateReply(@RequestBody BoardReply boardReply) {
        try{
            boardReplyService.updateReply(boardReply);
            return ResponseEntity.ok("Reply Insert Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Reply Insert Error");
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
