package com.gongzone.central.utils;

public class MySqlUtil {

	/**
	 * 테이블의 마지막 PK를 입력받아 인덱스 부분을 1 증가시켜 반환한다.
	 *
	 * @param pk Primary key
	 * @return 새로운 Primary key
	 */
	public static String generatePrimaryKey(String pk) {
		// 코드 파싱
		String code = pk.replaceAll("\\d", "");

		// 숫자 부분 추출, 1 더하기
		int sequence = Integer.parseInt(pk.substring(code.length())) + 1;

		// 0 갯수 계산
		int padding = pk.length() - code.length();

		return String.format("%s%0" + padding + "d", code, sequence);
	}

	/**
	 * 코드와 인덱스를 입력받아 적절한 PK 형태로 반환한다.
	 *
	 * @param code 코드
	 * @param seq  인덱스
	 * @return Primary Key
	 */
	public static String generatePrimaryKey(String code, int seq) {
		String formattedSequence = String.format("%06d", seq);
		return code + formattedSequence;
	}

	/**
	 * pk를 입력받아 인덱스 부분만 1 증가시켜 반환한다.
	 *
	 * @param pk Primary key
	 * @return 인덱스
	 */
	public static int getNextIdx(String pk) {
		if (pk == null) {
			return 1;
		} else {
			// 코드 파싱
			String code = pk.replaceAll("\\d", "");
			// 숫자 부분 추출, 1 증가
			return Integer.parseInt(pk.substring(code.length())) + 1;
		}
	}

}
