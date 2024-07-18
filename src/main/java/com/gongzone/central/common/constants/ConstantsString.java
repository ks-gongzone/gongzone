package com.gongzone.central.common.constants;

public enum ConstantsString {
	ADMIN_POINT_NO("MP000001", "어드민 포인트 번호"),
	MESSAGE_ALERT_PARTY_PURCHASE_LEADER("파티원이 모두 결제를 완료했습니다. 쇼핑몰에서 결제를 진행해주세요.", "파티장 결제 요청 알림"),
	MESSAGE_ALERT_PARTY_SHIPPING("쇼핑몰에서 배송이 시작되었습니다. 조금만 기다려주세요!", "쇼핑몰 배송 시작 알림"),
	MESSAGE_ALERT_PARTY_SHIPPING_COMPLETE("쇼핑몰에서 상품이 도착했습니다. 파티원들과 협의해서 물품을 수령해 주세요.", "쇼핑몰 배송 완료 알림"),
	MESSAGE_ALERT_PARTY_RECEPTION("파티원이 물품을 수령했습니다.", "파티원 수취확인 알림"),
	MESSAGE_ALERT_PARTY_RECEPTION_COMPLETE("모든 파티원이 물품을 수령했습니다. 정산은 자동으로 진행되며, 영업일 기준 2~3일 이내에 정산이 완료됩니다.", "파티 수취 완료 알림"),
	MESSAGE_ALERT_PARTY_SETTLEMENT_COMPLETE("파티 정산이 완료되었습니다.", "정산 완료 알림");


	private String value;
	private String description;

	ConstantsString(String value, String description) {
		this.value = value;
		this.description = description;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
