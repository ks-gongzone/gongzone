package com.gongzone.central.board.service;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardResponse;
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
    public void setValue(BoardResponse br) {
        Board board = new Board();
        board.setMemberNo(br.getMemberNo());
        board.setBoardTitle(br.getTitle());
        board.setCategory(br.getCategory());
        board.setProductUrl(br.getURL());
        board.setTotalPrice(br.getPrice());
        board.setTotal(br.getTotal());
        board.setAmount(br.getAmount());
        board.setBoardBody(br.getContent());
        board.setLocationDo(br.getDoCity());
        board.setLocationSi(br.getSiGun());
        board.setLocationGu(br.getGu());
        board.setLocationDong(br.getDong());
        board.setLocationDetail(br.getDetailAddress());
        board.setLocationX(br.getLatitude());
        board.setLocationY(br.getLongitude());
        board.setEndDate(br.getEndDate());

        boardMapper.insertBoard(board);
        boardMapper.insertLocation(board);
        boardMapper.insertParty(board);
        boardMapper.insertPartyMember(board);
    }
}
