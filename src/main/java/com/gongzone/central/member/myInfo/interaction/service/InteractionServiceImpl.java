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
    public List<InteractionMember> findAllMembers(
            String currentUserNo,
            String memberName,
            String searchQuery,
            int page,
            int size) {
        System.out.println("[서비스] findAllMembers 메서드: " + currentUserNo);
        int offset = (page - 1) * size;
        List<InteractionMember> members = interactionMapper.findAllMembers(currentUserNo, memberName, offset, size);
        for (InteractionMember member : members) {
            filterData(member, currentUserNo, member.getMemberNo(), searchQuery);
            System.out.println("[서비스] 회원 번호 " + member.getMemberNo() + " 인기유저 여부 " + member.isPopular());
        }
        return members;
    }
    @Override
    public InteractionMember filterData(InteractionMember member, String currentUserNo, String targetMemberNo, String searchQuery) {
        System.out.println("[서비스] 회원 데이터 필터링 현재회원번호: " + member.getCurrentUserNo());
        int warningCount = interactionMapper.getWarningCount(member.getMemberNo());
        boolean isWarning = warningCount > 10;

        System.out.println("검색어: " + searchQuery);

        // 상위 10%의 인기유저 관리하기 위한 로직
        int totalMembers = interactionMapper.getTotalMembers(member.getMemberName());
        int popularCount = (int) (totalMembers * 0.1);
        int memberFollowCount = interactionMapper.getMemberFollowCount(member.getMemberNo());
        boolean isPopular = memberFollowCount > popularCount;

        return InteractionMember.builder()
                .memberNo(member.getMemberNo())
                .memberName(member.getMemberName())
                .gender(member.getGender())
                .isFollowing(member.isFollowing())
                .isBlocked(member.isBlocked())
                .isWarning(isWarning)
                .isPopular(isPopular)
                .currentUserNo(currentUserNo)
                .targetMemberNo(targetMemberNo)
                .searchQuery(searchQuery)
                .build();
    }
    @Override
    public int getTotalMembers(String memberName) {
        return interactionMapper.getTotalMembers(memberName);
    }

    /**
     * @내용: 팔로잉 및 취소
     */
    @Override
    public void followMember(String currentUserNo, String targetMemberNo) {
        System.out.println("팔로우신청멤버:" + currentUserNo);
        System.out.println("타겟멤버:" + targetMemberNo);
        interactionMapper.insertFollow(currentUserNo, targetMemberNo);
    }
    @Override
    public void unFollowMember(String currentUserNo, String targetMemberNo) {
        System.out.println("언 팔로우신청멤버:" + currentUserNo);
        System.out.println("타겟멤버:" + targetMemberNo);
        interactionMapper.deleteFollow(currentUserNo, targetMemberNo);
    }

    @Override
    public void blockMember(String currentUserNo, String targetMemberNo) {
        System.out.println("차단시도멤버:" + currentUserNo);
        System.out.println("타겟멤버:" + targetMemberNo);
        interactionMapper.insertBlock(currentUserNo, targetMemberNo);
    }
    @Override
    public void unBlockMember(String currentUserNo, String targetMemberNo) {
        System.out.println("차단해제시도멤버:" + currentUserNo);
        System.out.println("타겟멤버:" + targetMemberNo);
        interactionMapper.deleteBlock(currentUserNo, targetMemberNo);
    }
}
