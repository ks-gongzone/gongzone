package com.gongzone.central.utils;

public class MySqlUtil {
    public static String generatePrimaryKey(String pk) {
        // 코드 파싱
        String code = pk.replaceAll("\\d", "");

        // 숫자 부분 추출, 1 더하기
        int sequence = Integer.parseInt(pk.substring(code.length())) + 1;

        // 0 갯수 계산
        int padding = pk.length() - code.length();

        return String.format("%s%0" + padding + "d", code, sequence);
    }

}
