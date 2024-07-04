package com.gongzone.central.board.mapper;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardSearchList;
import com.gongzone.central.board.domain.BoardSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    void insertBoard(Board board);
    void insertLocation(Board board);
    void insertFileRelation(Board board);
    void insertParty(Board board);
    void insertPartyMember(Board board);
    List<BoardSearchList> getBoardList(BoardSearchRequest request);
}
