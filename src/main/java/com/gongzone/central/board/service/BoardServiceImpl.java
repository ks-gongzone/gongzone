package com.gongzone.central.board.service;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardResponse;
import com.gongzone.central.board.domain.BoardSearchList;
import com.gongzone.central.board.domain.BoardSearchRequest;
import com.gongzone.central.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardMapper boardMapper;
//    private final FileMapper fileMapper;
//    private final FileUtil fileUtil;


    @Override
    public Map<String, List<BoardSearchList>> getBoardList(BoardSearchRequest request) {
        List<BoardSearchList> lists = boardMapper.getBoardList(request);
        Map<String, List<BoardSearchList>> result = new HashMap<>();
        result.put("result", lists);
        return result;
    }

    @Override
    @Transactional
    public void setValue(BoardResponse br, MultipartFile[] files) {
        Board board = Board.builder()
                            .memberNo(br.getMemberNo())
                            .boardTitle(br.getTitle())
                            .category(br.getCategory())
                            .productUrl(br.getURL())
                            .totalPrice(br.getPrice())
                            .total(br.getTotal())
                            .amount(br.getAmount())
                            .boardBody(br.getContent())
                            .locationDo(br.getDoCity())
                            .locationSi(br.getSiGun())
                            .locationGu(br.getGu())
                            .locationDong(br.getDong())
                            .locationDetail(br.getDetailAddress())
                            .locationX(br.getLatitude())
                            .locationY(br.getLongitude())
                            .endDate(br.getEndDate())
                            .build();

        boardMapper.insertBoard(board);
//        for (MultipartFile file : files) {
//            FileUpload fileUpload = fileUtil.parseFileInfo(file);
//            if (fileUpload != null) {
//                fileMapper.addFile(fileUpload);
//                // 추가로 board와 file 관계 저장 로직이 필요하면 작성
//            }
//        }
        boardMapper.insertFileRelation(board);
        boardMapper.insertLocation(board);
        boardMapper.insertParty(board);
        boardMapper.insertPartyMember(board);

    }
}
