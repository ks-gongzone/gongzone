package com.gongzone.central.member.alertSSE.domain;

import lombok.Data;

@Data
public class AlertAllow {
    private boolean allowSms;
    private boolean allowEmail;
    private boolean allowMarketing;
    private boolean allowMember;
    private boolean allowNote;
    private boolean allowBulletin;
    private boolean allowParty;
    private String memberNo;
}
