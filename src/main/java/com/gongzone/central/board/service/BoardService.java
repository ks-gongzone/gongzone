package com.gongzone.central.board.service;

import com.gongzone.central.board.domain.BoardSearch;

import java.util.List;
import java.util.Map;

public interface BoardService {
    Map<String, List<BoardSearch>> getBoardList(String location, String category, String content);
}
