package com.gongzone.central.location.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationSearch {
    private String boardTitle;
    private String latitude;
    private String longitude;
    private String address;
    private String detailAddress;
    private String remainAmount;
    private String price;
    private String partyNo;
    private Date endDate;
    private String filePath;
}
