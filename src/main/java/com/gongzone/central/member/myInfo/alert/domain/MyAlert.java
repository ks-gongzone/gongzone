package com.gongzone.central.member.myInfo.alert.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 개별 알람 설정
 * @date: 2024-06-18
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class MyAlert {
    private final int alertAllowNo;
    private final String memberNo;
    private boolean smsAlert;
    private boolean emailAlert;
    private boolean marketingAlert;
    private boolean memberAlert;
    private boolean noteAlert;
    private boolean bulletinAlert;
    private boolean partyAlert;

}
