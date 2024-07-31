package com.gongzone.central.member.myInfo.interaction.service;

import com.gongzone.central.member.myInfo.interaction.domain.InteractionMember;
import com.gongzone.central.member.myInfo.interaction.mapper.InteractionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InteractionServiceImpl implements InteractionService {

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
        int offset = (page - 1) * size;
        List<InteractionMember> allMembers = new ArrayList<>();
        int totalMembers = interactionMapper.getTotalMembers(memberName);

        while (allMembers.size() < size && offset < totalMembers) {
            List<InteractionMember> members = interactionMapper.findAllMembers(currentUserNo, memberName, offset, size);
            List<InteractionMember> filteredMembers = members.stream()
                    .filter(member -> !member.getMemberNo().equals("M000001") || currentUserNo.equals("M000001"))
                    .collect(Collectors.toList());

            allMembers.addAll(filteredMembers);
            offset += size;

            // 중복 추가 방지를 위해 size만큼 데이터를 가져온 후 필터링된 리스트로 업데이트
            if (allMembers.size() > size) {
                allMembers = allMembers.subList(0, size);
            }
        }

        // 최종 필터링된 리스트 반환
        return allMembers;
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
        interactionMapper.insertFollow(currentUserNo, targetMemberNo);
    }

    @Override
    public void unFollowMember(String currentUserNo, String targetMemberNo) {
        interactionMapper.deleteFollow(currentUserNo, targetMemberNo);
    }

    @Override
    public void blockMember(String currentUserNo, String targetMemberNo) {
        interactionMapper.insertBlock(currentUserNo, targetMemberNo);
    }

    @Override
    public void unBlockMember(String currentUserNo, String targetMemberNo) {
        interactionMapper.deleteBlock(currentUserNo, targetMemberNo);
    }
}
