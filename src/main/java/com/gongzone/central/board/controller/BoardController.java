package com.gongzone.central.board.controller;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardSearchList;
import com.gongzone.central.board.domain.BoardSearchRequest;
import com.gongzone.central.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@Slf4j
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) { this.boardService = boardService; }

    @PostMapping("/list")
    public ResponseEntity<Map<String, List<BoardSearchList>>> getBoardList(@RequestBody BoardSearchRequest request) {
        Map<String, List<BoardSearchList>> response = boardService.getBoardList(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/write/{memberNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> writeBoard(
            @PathVariable String memberNo,
            @RequestPart Board board,
            @RequestPart("image") MultipartFile[] image) throws IOException {
        try{
            log.info("image: {}", image.length);
            //boardService.createAll(board);

            return ResponseEntity.ok("Insert Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Insert Error");
        }
    }
}
