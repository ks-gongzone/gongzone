package com.gongzone.central.member.myInfo.interaction.service;

import com.gongzone.central.member.myInfo.interaction.domain.InteractionMember;

import java.util.List;

public interface InteractionService {
    InteractionMember getMemberByNo(String memberNo, String currentUserNo);
    InteractionMember filterData(InteractionMember member, String memberNo);
    List<InteractionMember> findAllMembers(String currenUserNo, String memberName, int page, int size);
}
