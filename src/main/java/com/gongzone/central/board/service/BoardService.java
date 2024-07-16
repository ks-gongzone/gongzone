package com.gongzone.central.board.service;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardReply;
import com.gongzone.central.board.domain.BoardResponse;
import com.gongzone.central.board.domain.BoardSearchRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardService {
    void deleteBoard(String boardNo, String partyNo);
    void updateBoardNoImage(String boardNo, BoardResponse br);
    void updateBoard(String boardNo, BoardResponse br, MultipartFile file);
    List<Board> getBoardInfo(String boardNo);
    void setWish(String boardNo, String memberNo);
    void updateViewCount(String boardNo);
    void setValue(BoardResponse br, MultipartFile file);
    List<Board> getBoardList(BoardSearchRequest request);
}
