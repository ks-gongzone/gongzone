package com.gongzone.central.board.service;

import com.gongzone.central.board.domain.BoardSearch;
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
    public Map<String, List<BoardSearch>> getAllList(String location, String category, String content){
        List<BoardSearch> Lists = boardMapper.getAllList(location, category, content);
        Map<String, List<BoardSearch>> result = new HashMap<>(
                Map.of("result", Lists)
        );

        return result;
    }
}
