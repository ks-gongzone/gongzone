package com.gongzone.central.member.myInfo.domain;

import lombok.Data;

/**
 * 개별 알람 설정
 * @date: 2024-06-18
 *
 */

@Data
public class MyAlert {
    private boolean SMSAlert;
    private boolean emailAlert;
    private boolean marketingAlert;
    private boolean memberAlert;
    private boolean noteAlert;
    private boolean bulletinAlert;
    private boolean partyAlert;
}
