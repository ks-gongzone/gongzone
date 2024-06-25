package com.gongzone.central.board.mapper;

import com.gongzone.central.board.domain.BoardSearch;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardSearch> getBoardList(String location, String category, String content);
}
