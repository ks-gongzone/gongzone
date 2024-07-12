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
    public List<InteractionMember> findAllMembers(String currentUserNo, String memberName, int page, int size) {
        System.out.println("[서비스] findAllMembers 메서드: " + currentUserNo);
        int offset = (page - 1) * size;
        List<InteractionMember> members = interactionMapper.findAllMembers(currentUserNo, memberName, offset, size);
        for (InteractionMember member : members) {
            filterData(member, currentUserNo, member.getMemberNo());
        }
        return members;
    }
    @Override
    public InteractionMember filterData(InteractionMember member, String currentUserNo, String targetMemberNo) {
        System.out.println("[서비스] 회원 데이터 필터링: " + member.getMemberNo());
        // 차단 10건 이상 유저 관리하기 위한 로직
        int warningCount = interactionMapper.getWarningCount(member.getMemberNo());
        boolean isWarning = warningCount > 10;

        // 상위 10%의 인기유저 관리하기 위한 로직
        int totalMembers = interactionMapper.getTotalMembers(member.getMemberNo());
        int popularCount = (int) (totalMembers * 0.1);
        int memberFollowCount = interactionMapper.getMemberFollowCount(member.getMemberNo());
        boolean isPopular = memberFollowCount > popularCount;

        System.out.println("[서비스] 회원 번호: " + member.getMemberNo() +
                ", 팔로우 수: " + memberFollowCount + ", 전체 회원 수: " +
                totalMembers + ", 인기 유저 여부: " + isPopular);

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
                .build();
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
