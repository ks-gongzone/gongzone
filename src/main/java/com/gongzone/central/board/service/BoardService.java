package com.gongzone.central.board.service;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardSearchList;
import com.gongzone.central.board.domain.BoardSearchRequest;

import java.util.List;
import java.util.Map;

public interface BoardService {
    Board createAll(Board board);
    Map<String, List<BoardSearchList>> getBoardList(BoardSearchRequest request);
}
