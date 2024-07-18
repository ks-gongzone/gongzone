package com.gongzone.central.member.myInfo.interaction.service;

import com.gongzone.central.member.myInfo.interaction.domain.InteractionMember;

import java.util.List;

public interface InteractionService {
    // read
    InteractionMember filterData(InteractionMember member, String currentUserNo, String targetMemberNo, String searchName);
    int getTotalMembers(String memberName);
    List<InteractionMember> findAllMembers(String currentUserNo, String memberName, String searchQuery, int page, int size);
    // following action
    void followMember(String currentUserNo, String targetMemberNo);
    void unFollowMember(String currentUserNo, String targetMemberNo);
    // blocking action
    void blockMember(String currentUserNo, String targetMemberNo);
    void unBlockMember(String currentUserNo, String targetMemberNo);
}
