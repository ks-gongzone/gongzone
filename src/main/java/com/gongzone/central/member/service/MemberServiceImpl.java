package com.gongzone.central.member.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.Token;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.member.mapper.TokenMapper;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.utils.MySqlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;
    private final PointMapper pointMapper;

    @Override
    public Boolean registerMember(Member member) {
        try {
            memberMapper.insert(member);

            String lastPointNo = pointMapper.getLastMemberPointNo();
            String newPointNo = MySqlUtil.generatePrimaryKey(lastPointNo);

            Point point = Point.builder()
                    .memberPointNo(newPointNo)
                    .memberNo(member.getMemberNo())
                    .memberPoint("0")
                    .build();

            pointMapper.insertPoint(point);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*private String giveMemberNo() {
        int number = memberMapper.findMemberNo();
        return "M" + (number + 1);
    }*/

    @Override
    public List<Member> getAllMembers() {
        return memberMapper.findAll();
    }

   /* @Override
    public Member getMemberByNo(String memberNo) {
        return memberMapper.findByNo(memberNo);
    }*/

    @Override
    public Member getMemberByNo(String memberNo) {
        return memberMapper.info(memberNo);
    }

    @Override
    public Boolean getMemberById(String memberId) {
        return memberMapper.findById(memberId);
    }

    @Override
    public void updateMember(Member member) {
        memberMapper.update(member);
    }

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
