package com.gongzone.central.utils;

public enum TypeCode {
	T010101("회원", "로그인", "자사몰"),
	T010102("회원", "로그인", "네이버"),
	T010103("회원", "로그인", "구글"),
	T010104("회원", "로그인", "카카오"),
	T010201("회원", "알림", "공지"),
	T010202("회원", "알림", "광고성"),
	T010203("회원", "알림", "회원"),
	T010204("회원", "알림", "쪽지"),
	T010205("회원", "알림", "게시글"),
	T010206("회원", "알림", "파티"),
	T010207("회원", "알림", "SMS"),
	T010208("회원", "알림", "이메일"),
	T010301("회원", "탈퇴", "서비스 불만족"),
	T010302("회원", "탈퇴", "기타"),
	T010401("회원", "제재", "부적절한 콘텐츠"),
	T010402("회원", "제재", "사기 및 사기성 행위"),
	T010403("회원", "제재", "스팸 및 악성 행위"),
	T010404("회원", "제재", "지적 재산권 침해"),
	T010405("회원", "제재", "사생활 침해 및 개인정보 보호"),
	T010406("회원", "제재", "사용자 행위 관련"),
	T010407("회원", "제재", "기타"),
	T010501("회원", "신고", "부적절한 콘텐츠"),
	T010502("회원", "신고", "사기 및 사기성 행위"),
	T010503("회원", "신고", "스팸 및 악성 행위"),
	T010504("회원", "신고", "지적 재산권 침해"),
	T010505("회원", "신고", "사생활 침해 및 개인정보 보호"),
	T010506("회원", "신고", "사용자 행위 관련"),
	T010507("회원", "신고", "기타"),
	T010601("회원", "문의", "회원"),
	T010602("회원", "문의", "게시글"),
	T010603("회원", "문의", "파티"),
	T010604("회원", "문의", "기타"),
	T020101("관리자", "게시글", "공지사항"),
	T020102("관리자", "게시글", "FAQ"),
	T020103("관리자", "게시글", "프로모션"),
	T030101("포인트", "증가", "충전"),
	T030102("포인트", "증가", "프로모션"),
	T030103("포인트", "증가", "환불"),
	T030104("포인트", "증가", "파티 정산"),
	T030201("포인트", "증가(관리자)", "사용자 회수"),
	T030202("포인트", "증가(관리자)", "파티 입금"),
	T030301("포인트", "감소", "인출"),
	T030302("포인트", "감소", "회수"),
	T030303("포인트", "감소", "파티 결제"),
	T030401("포인트", "감소(관리자)", "프로모션지급"),
	T030402("포인트", "감소(관리자)", "사용자 환불"),
	T030403("포인트", "감소(관리자)", "파티장 입금");


	private final String codeGroup;
	private final String codeGroupSub;
	private final String codeDescription;

	TypeCode(String codeGroup, String codeGroupSub, String codeDescription) {
		this.codeGroup = codeGroup;
		this.codeGroupSub = codeGroupSub;
		this.codeDescription = codeDescription;
	}

	public static String getDescriptionByCode(String code) {
		for (TypeCode typeCode : values()) {
			if (typeCode.name().equals(code)) {
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
