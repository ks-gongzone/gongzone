package com.gongzone.central.board.controller;

import com.gongzone.central.board.domain.BoardSearch;
import com.gongzone.central.board.service.BoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) { this.boardService = boardService; }

    @GetMapping("/search")
    public Map<String, List<BoardSearch>> getBoardList(@RequestParam String location, @RequestParam String category, @RequestParam String content) {
        Map<String, List<BoardSearch>> response = boardService.getAllList(location, category, content);

        return response;
    }
}
