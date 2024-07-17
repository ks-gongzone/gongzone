package com.gongzone.central.board.admin.controller;

import com.gongzone.central.board.admin.domain.BoardAdminInfo;
import com.gongzone.central.board.admin.domain.BoardProgress;
import com.gongzone.central.board.admin.service.BoardAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@Slf4j
public class BoardAdminController {
    private final BoardAdminService boardAdminService;

    public BoardAdminController(BoardAdminService boardAdminService) { this.boardAdminService = boardAdminService; }

    @PostMapping("/_admin")
    public BoardAdminInfo getBoardAdminInfo() {
        BoardAdminInfo response = boardAdminService.getBoardAdminInfo();
        return response;
    }
}
