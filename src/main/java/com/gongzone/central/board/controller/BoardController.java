package com.gongzone.central.board.controller;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardSearchList;
import com.gongzone.central.board.domain.BoardSearchRequest;
import com.gongzone.central.board.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) { this.boardService = boardService; }

    @PostMapping("/list")
    public ResponseEntity<Map<String, List<BoardSearchList>>> getBoardList(@RequestBody BoardSearchRequest request) {
        Map<String, List<BoardSearchList>> response = boardService.getBoardList(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/write/{memberNo}")
    public ResponseEntity<String> writeBoard(@RequestBody Board board, @PathVariable String memberNo) {
        try{
            boardService.createAll(board);

            return ResponseEntity.ok("Insert Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Insert Error");
        }
    }
}
