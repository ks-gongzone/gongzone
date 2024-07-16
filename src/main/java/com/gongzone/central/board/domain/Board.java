package com.gongzone.central.board.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongzone.central.file.domain.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Board {
    //  게시글 T
    private String boardNo;
    private String memberNo;
    private String boardTitle;
    private String boardBody;
    private Date startDate;
    private LocalDateTime endDate;
    private int viewCount;
    private int wishCount;
    private int reportCount;
    private String boardStatus;

    //  파일 관계 T
    private int fileRelationNo;
    private int fileNo;
    private String fileUsage;

    //  위치 T
    private int locationNo;
    private String locationDo;
    private String locationSi;
    private String locationGu;
    private String locationDong;
    private String locationDetail;
    private Double locationX;
    private Double locationY;

    //  파티 정보 T
    private String partyNo;
    private String category;
    private String productUrl;
    private int total;
    private int remain;
    private int totalPrice;
    private int remainPrice;
    private Date partyEnd;
    private String partyStatus;

    //  파티원 정보 T
    private String partyMemberNo;
    private int amount;
    private int amountPrice;
    private String leader;

    //  파일 T
    private List<FileUpload> files;

    //  댓글 T
    private List<BoardReply> replies;

    //  찜 여부
    private boolean wish;
}
