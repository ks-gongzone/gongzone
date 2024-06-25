package com.gongzone.central.utils;

import lombok.Getter;

public enum TypeCode {
    TYPE_1("T010101", "회원", "로그인", "자사몰"),
    TYPE_2("T010102", "회원", "로그인", "네이버"),
    TYPE_3("T010103", "회원", "로그인", "구글"),
    TYPE_4("T010104", "회원", "로그인", "카카오"),
    TYPE_5("T010201", "회원", "알림", "공지"),
    TYPE_6("T010202", "회원", "알림", "광고성"),
    TYPE_7("T010203", "회원", "알림", "회원"),
    TYPE_8("T010204", "회원", "알림", "쪽지"),
    TYPE_9("T010205", "회원", "알림", "게시글"),
    TYPE_10("T010206", "회원", "알림", "파티"),
    TYPE_11("T010207", "회원", "알림", "SMS"),
    TYPE_12("T010208", "회원", "알림", "이메일"),
    TYPE_13("T010301", "회원", "탈퇴", "서비스 불만족"),
    TYPE_14("T010302", "회원", "탈퇴", "기타"),
    TYPE_15("T010401", "회원", "제재", "부적절한 콘텐츠"),
    TYPE_16("T010402", "회원", "제재", "사기 및 사기성 행위"),
    TYPE_17("T010403", "회원", "제재", "스팸 및 악성 행위"),
    TYPE_18("T010404", "회원", "제재", "지적 재산권 침해"),
    TYPE_19("T010405", "회원", "제재", "사생활 침해 및 개인정보 보호"),
    TYPE_20("T010406", "회원", "제재", "사용자 행위 관련"),
    TYPE_21("T010407", "회원", "제재", "기타"),
    TYPE_22("T010501", "회원", "신고", "부적절한 콘텐츠"),
    TYPE_23("T010502", "회원", "신고", "사기 및 사기성 행위"),
    TYPE_24("T010503", "회원", "신고", "스팸 및 악성 행위"),
    TYPE_25("T010504", "회원", "신고", "지적 재산권 침해"),
    TYPE_26("T010505", "회원", "신고", "사생활 침해 및 개인정보 보호"),
    TYPE_27("T010506", "회원", "신고", "사용자 행위 관련"),
    TYPE_28("T010507", "회원", "신고", "기타"),
    TYPE_29("T010601", "회원", "문의", "회원"),
    TYPE_31("T010602", "회원", "문의", "게시글"),
    TYPE_32("T010603", "회원", "문의", "파티"),
    TYPE_33("T010604", "회원", "문의", "기타"),
    TYPE_34("T020101", "관리자", "게시글", "공지사항"),
    TYPE_35("T020102", "관리자", "게시글", "FAQ"),
    TYPE_36("T020103", "관리자", "게시글", "프로모션"),
    TYPE_POINT_INCREASE_CHARGE("T030101", "포인트", "증가", "충전"),
    TYPE_POINT_INCREASE_PROMOTION("T030102", "포인트", "증가", "프로모션"),
    TYPE_POINT_INCREASE_REFUND("T030103", "포인트", "증가", "환불"),
    TYPE_POINT_INCREASE_SETTLEMENT("T030104", "포인트", "증가", "파티 정산"),
    TYPE_POINT_INCREASE_ADMIN_1("T030201", "포인트", "증가(관리자)", "사용자 회수"),
    TYPE_POINT_INCREASE_ADMIN_2("T030202", "포인트", "증가(관리자)", "파티 입금"),
    TYPE_POINT_DECREASE_WITHDRAW("T030301", "포인트", "감소", "인출"),
    TYPE_POINT_DECREASE_("T030302", "포인트", "감소", "회수"),
    TYPE_POINT_DECREASE_PAYMENT("T030303", "포인트", "감소", "파티 결제"),
    TYPE_POINT_DECREASE_ADMIN_1("T030401", "포인트", "감소(관리자)", "프로모션지급"),
    TYPE_POINT_DECREASE_ADMIN_2("T030402", "포인트", "감소(관리자)", "사용자 환불"),
    TYPE_POINT_DECREASE_ADMIN_3("T030403", "포인트", "감소(관리자)", "파티장 입금");

    @Getter

    private final String code;
    private final String codeGroup;
    private final String codeGroupSub;
    private final String codeDescription;

    TypeCode(String code, String codeGroup, String codeGroupSub, String codeDescription) {
        this.code = code;
        this.codeGroup = codeGroup;
        this.codeGroupSub = codeGroupSub;
        this.codeDescription = codeDescription;
    }

    public static String getDescriptionByCode(String code) {
        for (TypeCode typeCode : values()) {
            if (typeCode.code.equals(code)) {
                return typeCode.toString();
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }

    @Override
    public String toString() {
        return codeDescription;
    }

}
