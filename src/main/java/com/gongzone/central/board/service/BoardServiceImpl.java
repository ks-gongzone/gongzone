package com.gongzone.central.board.service;

import com.gongzone.central.board.domain.*;
import com.gongzone.central.board.mapper.BoardMapper;
import com.gongzone.central.file.domain.FileUpload;
import com.gongzone.central.file.mapper.FileMapper;
import com.gongzone.central.file.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;
    private final FileUtil fileUtil;

    @Override
    @Transactional
    public void deleteBoard(String boardNo, String partyNo) {
        int countPm = boardMapper.countPartyMember(partyNo);

        if (countPm == 0) {
            boardMapper.deleteParty(partyNo);
            boardMapper.deleteBoard(boardNo);
        } else {
            throw new IllegalStateException("파티원이 있는 게시글은 삭제할 수 없습니다.");
        }

    }

    @Override
    @Transactional
    public void updateBoardNoImage(String boardNo, BoardResponse br) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String endDateString = br.getEndDate().replace("T", " ");
        LocalDateTime endDateTime = LocalDateTime.parse(endDateString, formatter);

        Board board = Board.builder()
                .memberNo(br.getMemberNo())
                .boardNo(boardNo)
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


        board.setRemain(board.getTotal() - board.getAmount());
        int unitPrice = (int) Math.ceil((double) board.getTotalPrice() / board.getTotal());
        board.setRemainPrice(board.getTotalPrice() - (unitPrice * board.getAmount()));

        board.setAmountPrice(unitPrice * board.getAmount());

        board.setPartyNo(boardMapper.getPartyNo(boardNo));

        boardMapper.updatePartyMember(board);
        boardMapper.updateParty(board);
        boardMapper.updateLocation(board);
        boardMapper.updateBoard(board);
    }

    @Override
    @Transactional
    public void updateBoard(String boardNo, BoardResponse br, MultipartFile file) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String endDateString = br.getEndDate().replace("T", " ");
        LocalDateTime endDateTime = LocalDateTime.parse(endDateString, formatter);

        Board board = Board.builder()
                .memberNo(br.getMemberNo())
                .boardNo(boardNo)
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


        board.setRemain(board.getTotal() - board.getAmount());
        int unitPrice = (int) Math.ceil((double) board.getTotalPrice() / board.getTotal());
        board.setRemainPrice(board.getTotalPrice() - (unitPrice * board.getAmount()));

        board.setAmountPrice(unitPrice * board.getAmount());

        board.setPartyNo(boardMapper.getPartyNo(boardNo));

        boardMapper.updatePartyMember(board);
        boardMapper.updateParty(board);
        boardMapper.updateLocation(board);

        FileUpload fileUpload = fileUtil.parseFileInfo(file);
        fileUpload.setFileIdx(boardMapper.getFileNo(boardNo));

        fileMapper.updateFile(fileUpload);

        boardMapper.updateBoard(board);
    }

    @Override
    public List<Board> getBoardInfo(String boardNo) {
        System.out.println(boardNo);
        List<Board> response = boardMapper.getBoardInfo(boardNo);
        return response;
    }

    @Override
    public void setWish(String boardNo, String memberNo) {
        int wishInt = boardMapper.getBoardWish(memberNo, boardNo);

        if (wishInt == 1) {
            boardMapper.deleteWish(boardNo, memberNo);
        } else {
            boardMapper.insertWish(boardNo, memberNo);
        }
    }

    @Override
    public void updateViewCount(String boardNo) {
        boardMapper.updateViewCount(boardNo);
    }

    @Override
    public List<Board> getBoardList(BoardSearchRequest request) {
        System.out.println(request);
        List<Board> lists = boardMapper.getBoardList(request);

        String memberNo = request.getMemberNo();

        lists.forEach(board -> {
            List<FileUpload> files = fileMapper.getBoardFileList(board.getFileNo());
            board.setFiles(files);
        });

        lists.forEach(board -> {
            List<BoardReply> replies = boardMapper.getBoardReplyList(board.getBoardNo());
            board.setReplies(replies);
        });

        lists.forEach(board -> {
            int wishInt = boardMapper.getBoardWish(memberNo, board.getBoardNo());
            if (wishInt == 1) {
                boolean wish = true;
                board.setWish(wish);
            } else {
                boolean wish = false;
                board.setWish(wish);
            }

        });
        return lists;
    }

    @Override
    public List<BoardMyPage> getWishListMypage(String memberNo) {
        List<BoardMyPage> boardMyPages = boardMapper.wishListMyPage(memberNo);
        boardMyPages.forEach(boardMyPage -> {
            String boardNo = boardMyPage.getBoardNo();
            String filePath = getfilePathByboardNo(boardNo);
            boardMyPage.setFilePath(filePath);
        });
        return boardMyPages;
    }

    @Override
    public String getfilePathByboardNo(String boardNo) {
        return boardMapper.filePathByboardNo(boardNo);
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
        fileMapper.addFile(fileUpload);

        board.setFileNo(fileUpload.getFileIdx());
        board.setFileUsage(board.getBoardNo());
        boardMapper.insertFileRelation(board);

        boardMapper.insertLocation(board);

        board.setRemain(board.getTotal() - board.getAmount());
        int unitPrice = (int) Math.ceil((double) board.getTotalPrice() / board.getTotal());
        board.setRemainPrice(board.getTotalPrice() - (unitPrice * board.getAmount()));
        boardMapper.insertParty(board);

        board.setAmountPrice(unitPrice * board.getAmount());
        boardMapper.insertPartyMember(board);

    }
}
