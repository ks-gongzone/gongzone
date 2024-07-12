package com.gongzone.central.board.controller;

import com.gongzone.central.board.domain.BoardReply;
import com.gongzone.central.board.domain.BoardResponse;
import com.gongzone.central.board.domain.BoardSearchRequest;
import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@Slf4j
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("/wish/{boardNo}/{memberNo}")
    public ResponseEntity<String> setWish(@PathVariable("boardNo") String boardNo,@PathVariable("memberNo") String memberNo) {
        try{
            boardService.setWish(boardNo, memberNo);
            return ResponseEntity.ok("WishList Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("WishList Error");
        }
    }

    @PostMapping("/addView/{boardNo}")
    public ResponseEntity<String> addView(@PathVariable("boardNo") String boardNo) {
        try{
            boardService.updateViewCount(boardNo);
            return ResponseEntity.ok("Update ViewCount Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Update ViewCount Error");
        }
    }

    @PostMapping("/list")
    public ResponseEntity<List<Board>> getBoardList(@RequestBody BoardSearchRequest request) {
        try {
            log.info("request {}", request);
            List<Board> response = boardService.getBoardList(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(null);
        }

    }

    @PostMapping(value = "/write/{memberNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> writeBoard(
            BoardResponse br,
            @RequestParam(value = "image") MultipartFile file) throws IOException {
        try {
            log.info("boardResponse: {}", br);
            log.info("file: {}", file);

            boardService.setValue(br, file);
            return ResponseEntity.ok("Insert Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Insert Error");
        }
    }
}
