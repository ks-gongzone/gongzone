package com.gongzone.central.board.controller;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardSearchList;
import com.gongzone.central.board.domain.BoardSearchRequest;
import com.gongzone.central.board.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<String> writeBoard(
            @PathVariable String memberNo,
            @RequestPart("board") Board board,
            @RequestPart("image") List<MultipartFile> image) throws IOException {
        System.out.println("Member No: " + board.getMemberNo());
        System.out.println("Board Title: " + board.getBoardTitle());
        try{
            boardService.createAll(board);

            return ResponseEntity.ok("Insert Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Insert Error");
        }
    }
}
