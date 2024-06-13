package com.gongzone.central.member.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.Token;

import java.util.List;

public interface MemberService {

    Member registerMember(Member member);
    List<Member> getAllMembers();
    Member getMemberByNo(String memberNo);
    void updateMember(Member member);
    void deleteMember(String memberNo);
    Token registerToken(Token token);
    List<Token> getAllTokens();
    Token getTokenByMemberNo(String memberNo);
    void updateToken(Token token);
    void deleteToken(int tokenNo);

}
