package com.gongzone.central.board.service;

import com.gongzone.central.board.domain.Board;
import com.gongzone.central.board.domain.BoardResponse;
import com.gongzone.central.board.domain.BoardSearchList;
import com.gongzone.central.board.domain.BoardSearchRequest;
import com.gongzone.central.board.mapper.BoardMapper;
import com.gongzone.central.file.mapper.FileMapper;
import com.gongzone.central.file.util.FileUtil;
import com.gongzone.central.file.domain.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;
    private final FileUtil fileUtil;

    @Override
    public Map<String, List<BoardSearchList>> getBoardList(BoardSearchRequest request) {
        List<BoardSearchList> lists = boardMapper.getBoardList(request);
        Map<String, List<BoardSearchList>> result = new HashMap<>();
        result.put("result", lists);
        return result;
    }

    @Override
    @Transactional
    public void setValue(BoardResponse br, MultipartFile file) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime endDateTime = LocalDateTime.parse(br.getEndDate(), formatter);
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
                            .endDate(endDateTime)
                            .build();

        boardMapper.insertBoard(board);

        FileUpload fileUpload = fileUtil.parseFileInfo(file);
        if(fileUpload != null) fileMapper.addFile(fileUpload);
        boardMapper.insertFileRelation(board);
        boardMapper.insertLocation(board);
        boardMapper.insertParty(board);
        boardMapper.insertPartyMember(board);

    }
}
