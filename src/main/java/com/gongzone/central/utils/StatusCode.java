package com.gongzone.central.utils;

import lombok.Getter;

public enum StatusCode {
    STATUS_1("S010101", "회원", "목록", "정상"),
    STATUS_2("S010102", "회원", "목록", "휴면"),
    STATUS_3("S010103", "회원", "목록", "제재"),
    STATUS_4("S010104", "회원", "목록", "탈퇴"),
    STATUS_5("S010201", "회원", "로그인", "로그인 실패"),
    STATUS_6("S010202", "회원", "로그인", "로그인 중"),
    STATUS_7("S010203", "회원", "로그인", "로그아웃"),
    STATUS_8("S010301", "회원", "쪽지", "수신"),
    STATUS_9("S010302", "회원", "쪽지", "읽음"),
    STATUS_11("S010303", "회원", "쪽지", "삭제"),
    STATUS_12("S010401", "회원", "알림", "수신"),
    STATUS_13("S010402", "회원", "알림", "읽음"),
    STATUS_14("S010403", "회원", "알림", "삭제"),
    STATUS_15("S010501", "회원", "제재", "처리 대기중"),
    STATUS_16("S010502", "회원", "제재", "수감 중"),
    STATUS_17("S010503", "회원", "제재", "석방 완료"),
    STATUS_18("S010601", "회원", "신고 접수", "처리 대기중"),
    STATUS_19("S010602", "회원", "신고 접수", "처리 완료"),
    STATUS_20("S010603", "회원", "신고 접수", "보류"),
    STATUS_21("S010701", "회원", "문의", "처리 대기중"),
    STATUS_22("S010702", "회원", "문의", "처리 완료"),
    STATUS_23("S010703", "회원", "문의", "보류"),
    STATUS_POINT_HISTORY_SUCCESS("S030101", "포인트", "증감 내역", "성공"),
    STATUS_POINT_HISTORY_FAILED("S030102", "포인트", "증감 내역", "실패"),
    STATUS_POINT_CHARGE_1("S030201", "포인트", "충전 내역", "성공"),
    STATUS_POINT_CHARGE_2("S030202", "포인트", "충전 내역", "잔액 부족"),
    STATUS_POINT_CHARGE_3("S030203", "포인트", "충전 내역", "결제 취소"),
    STATUS_POINT_CHARGE_4("S030204", "포인트", "충전 내역", "은행 점검"),
    STATUS_POINT_CHARGE_5("S030205", "포인트", "충전 내역", "네트워크 오류"),
    STATUS_POINT_CHARGE_6("S030206", "포인트", "충전 내역", "서비스 오류"),
    STATUS_POINT_WITHDRAW_1("S030301", "포인트", "인출 내역", "성공"),
    STATUS_POINT_WITHDRAW_2("S030302", "포인트", "인출 내역", "포인트 부족"),
    STATUS_POINT_WITHDRAW_3("S030303", "포인트", "인출 내역", "결제 취소"),
    STATUS_POINT_WITHDRAW_4("S030304", "포인트", "인출 내역", "은행 점검"),
    STATUS_POINT_WITHDRAW_5("S030305", "포인트", "인출 내역", "네트워크 오류"),
    STATUS_POINT_WITHDRAW_6("S030306", "포인트", "인출 내역", "서비스 오류"),
    STATUS_38("S040101", "게시글", "목록", "모집중"),
    STATUS_39("S040102", "게시글", "목록", "모집 완료"),
    STATUS_40("S040103", "게시글", "목록", "완료"),
    STATUS_41("S040104", "게시글", "목록", "임시 차단"),
    STATUS_42("S040105", "게시글", "목록", "삭제"),
    STATUS_43("S040106", "게시글", "목록", "기한 만료"),
    STATUS_44("S050101", "댓글", "목록", "게시 완료"),
    STATUS_45("S050102", "댓글", "목록", "수정 완료"),
    STATUS_46("S050103", "댓글", "목록", "임시 차단"),
    STATUS_47("S050104", "댓글", "목록", "삭제"),
    STATUS_48("S060101", "파티", "정상", "모집중"),
    STATUS_49("S060102", "파티", "정상", "모집완료"),
    STATUS_50("S060103", "파티", "정상", "파티원 결제대기"),
    STATUS_51("S060104", "파티", "정상", "파티장 결제대기"),
    STATUS_52("S060105", "파티", "정상", "쇼핑몰 배송중"),
    STATUS_53("S060106", "파티", "정상", "수취 대기중"),
    STATUS_54("S060107", "파티", "정상", "정산 대기중"),
    STATUS_55("S060108", "파티", "정상", "정산 완료"),
    STATUS_56("S060109", "파티", "비정상", "파티 해제"),
    STATUS_57("S060110", "파티", "비정상", "기간 만료"),
    STATUS_58("S060201", "파티", "신청 현황", "수락 대기중"),
    STATUS_59("S060202", "파티", "신청 현황", "가입완료"),
    STATUS_60("S060203", "파티", "신청 현황", "거절"),
    STATUS_61("S060204", "파티", "신청 현황", "본인 취소"),
    STATUS_62("S060205", "파티", "신청 현황", "강퇴"),
    STATUS_63("S060301", "파티", "결제", "대기"),
    STATUS_64("S060302", "파티", "결제", "완료"),
    STATUS_65("S060303", "파티", "결제", "취소"),
    STATUS_66("S060401", "파티", "상품 배송", "배송전"),
    STATUS_67("S060402", "파티", "상품 배송", "배송중"),
    STATUS_68("S060403", "파티", "상품 배송", "배송 완료"),
    STATUS_69("S060501", "파티", "상품 수취", "대기"),
    STATUS_70("S060502", "파티", "상품 수취", "완료"),
    STATUS_71("S060503", "파티", "상품 수취", "이슈"),
    STATUS_72("S060601", "파티", "정산", "정산 대기중"),
    STATUS_73("S060602", "파티", "정산", "정산 완료"),
    STATUS_74("S060603", "파티", "정산", "환불 요청"),
    STATUS_75("S060604", "파티", "정산", "환불 완료"),
    STATUS_76("S060605", "파티", "정산", "오류 발생");


    @Getter
    private final String code;
    private final String codeGroup;
    private final String codeGroupSub;
    private final String codeDescription;

    StatusCode(String code, String codeGroup, String codeGroupSub, String codeDescription) {
        this.code = code;
        this.codeGroup = codeGroup;
        this.codeGroupSub = codeGroupSub;
        this.codeDescription = codeDescription;
    }

    public static String getDescriptionByCode(String code) {
        for (StatusCode statusCode : values()) {
            if (statusCode.code.equals(code)) {
                return statusCode.toString();
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }

    @Override
    public String toString() {
        return codeDescription;
    }
  
}

