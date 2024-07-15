package com.gongzone.central.board.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
public class BoardResponse {
    private String memberNo;
    private String title;
    private String category;
    private String URL;
    private int price;
    private int total;
    private int amount;
    private String content;
    private String doCity;
    private String siGun;
    private String gu;
    private String dong;
    private String detailAddress;
    private Double latitude;
    private Double longitude;
    private String endDate;
}
