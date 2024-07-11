package com.gongzone.central.member.myInfo.interaction.service;

import com.gongzone.central.member.myInfo.interaction.domain.InteractionMember;
import com.gongzone.central.member.myInfo.interaction.mapper.InteractionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InteractionServiceImpl implements InteractionService{

    private final InteractionMapper interactionMapper;
    /**
     * @작성일: 2024-07-11
     * @수정일: 2024-07-11
     * @내용: 전체 유저 조회 및 유저 관리 위한 서비스레이어
     */
    @Override
    @Transactional(readOnly = true)
    public InteractionMember getMemberByNo(String memberNo, String currentUserNo) {
        System.out.println("[서비스] 상호작용 시 유저 조회 " + currentUserNo);
        InteractionMember member = interactionMapper.findMemberByNo(memberNo, currentUserNo);
        if(member == null) {
            return null;
        }
        return filterData(member, memberNo);
    }
    @Override
    @Transactional(readOnly = true)
    public List<InteractionMember> findAllMembers(String currentUserNo, String memberName, int page, int size) {
        System.out.println("[서비스] findAllMembers 메서드: " + currentUserNo);
        int offset = (page - 1) * size;
        List<InteractionMember> members = interactionMapper.findAllMembers(currentUserNo, memberName, offset, size);
        for (InteractionMember member : members) {
            filterData(member, member.getMemberNo());
        }
        return members;
    }
    @Override
    public InteractionMember filterData(InteractionMember member, String memberNo) {
        System.out.println("[서비스] 회원 데이터 필터링: " + member);
        // 차단 10건 이상 유저 관리하기 위한 로직
        int warningCount = interactionMapper.getWarningCount(memberNo);
        boolean isWarning = warningCount > 10;

        // 상위 10%의 인기유저 관리하기 위한 로직
        int totalMembers = interactionMapper.getTotalMembers(memberNo);
        int popularCount = (int) (totalMembers * 0.1);
        int memberFollowCount = interactionMapper.getMemberFollowCount(memberNo);
        boolean isPopular = memberFollowCount > popularCount;

        return InteractionMember.builder()
                .memberNo(member.getMemberNo())
                .memberName(member.getMemberName())
                .gender(member.getGender())
                .isFollowing(member.isFollowing())
                .isBlocked(member.isBlocked())
                .isWarning(isWarning)
                .isPopular(isPopular)
                .build();
    }
}
