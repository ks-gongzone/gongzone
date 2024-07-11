package com.gongzone.central.member.myInfo.interaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class InteractionMember {
    private final String memberNo; // memberNo와 targetMemberNo 구분해서 사용
    private final String memberName;
    private final char gender; // 성별은 남,녀를 M와 F으로 구분 male과 female
    private final boolean isFollowing;
    private final boolean isBlocked;
    private final boolean isWarning; // target_no 컬럼에 10개 이상 존재 시 주의요망 유저
    private final boolean isPopular; // 팔로우 당한 수 상위 10% 이내 일 시 인기 유저
}
