package com.gongzone.central.board.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Board {
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

    private int fileRelationNo;
    private int fileNo;
    private String fileUsage;

    private int locationNo;
    private String locationDo;
    private String locationSi;
    private String locationGu;
    private String locationDong;
    private String locationDetail;
    private Double locationX;
    private Double locationY;

    private String partyNo;
    private String category;
    private String productUrl;
    private int total;
    private int remain;
    private int totalPrice;
    private int remainPrice;
    private Date partyEnd;
    private String partyStatus;

    private String partyMemberNo;
    private int amount;
    private int amountPrice;
    private String leader;
}
