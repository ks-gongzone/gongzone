package com.gongzone.central.member.domain;

import lombok.Getter;

@Getter
public enum MemberLevel {
    MEMBER("LEVEL_MEMBER", 1),
    ADMIN("LEVEL_ADMIN", 2);

    private final String levelName;
    private final int level;

    MemberLevel(String levelName, int level) {
        this.levelName = levelName;
        this.level = level;
    }

    // db에서 가져와 enum 클래스 이름으로 변환
    public static MemberLevel fromLevel(int level) {
        for (MemberLevel memberLevel : MemberLevel.values()) {
            if (memberLevel.getLevel() == level) {
                return memberLevel;
            }
        }
        throw new IllegalArgumentException("Invalid member level: " + level);
    }
}
