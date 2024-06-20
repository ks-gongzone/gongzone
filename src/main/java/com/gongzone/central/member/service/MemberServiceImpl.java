package com.gongzone.central.member.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.Token;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.member.mapper.TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;

    public MemberServiceImpl(MemberMapper memberMapper, TokenMapper tokenMapper) {
        this.memberMapper = memberMapper;
        this.tokenMapper = tokenMapper;
    }

    @Override
    public Member registerMember(Member member) {
        memberMapper.insert(member);
        return member;
    }

    /*private String giveMemberNo() {
        int number = memberMapper.findMemberNo();
        return "M" + (number + 1);
    }*/

    @Override
    public List<Member> getAllMembers() {
        return memberMapper.findAll();
    }

    @Override
    public Member getMemberByNo(String memberNo) {
        return memberMapper.findByNo(memberNo);
    }

    @Override
    public void updateMember(Member member) { memberMapper.update(member); }

    @Override
    public void deleteMember(String memberNo) {
        memberMapper.delete(memberNo);
    }

    @Override
    public Token registerToken(Token token) {
        token.setTokenLastUpdate(new Date());
        tokenMapper.insert(token);
        return token;
    }

    @Override
    public List<Token> getAllTokens() {
        return tokenMapper.findAll();
    }

    @Override
    public Token getTokenByMemberNo(String memberNo) {
        return tokenMapper.findByMemberNo(memberNo);
    }

    @Override
    public void updateToken(Token token) {
        token.setTokenLastUpdate(new Date());
        tokenMapper.update(token);
    }

    @Override
    public void deleteToken(int tokenNo) {
        tokenMapper.delete(tokenNo);
    }
}
