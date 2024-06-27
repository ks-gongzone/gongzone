package com.gongzone.central.board.service;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardSearchList;
import com.gongzone.central.board.domain.BoardSearchRequest;
import com.gongzone.central.board.mapper.BoardMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BoardServiceImpl implements BoardService{
    private final BoardMapper boardMapper;

    public BoardServiceImpl(BoardMapper boardMapper) { this.boardMapper = boardMapper; }

    @Override
    public Map<String, List<BoardSearchList>> getBoardList(BoardSearchRequest request){
        List<BoardSearchList> lists = boardMapper.getBoardList(request);
        Map<String, List<BoardSearchList>> result = new HashMap<>();
        result.put("result", lists);
        return result;
    }

    @Override
    @Transactional
    public Board createAll(Board board) {
        boardMapper.insertBoard(board);
        boardMapper.insertImage(board);
        boardMapper.insertLocation(board);

        return board;
    }
}
