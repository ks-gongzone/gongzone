package com.gongzone.central.member.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.Token;
import com.gongzone.central.member.mapper.MemberMapper;
import com.gongzone.central.member.mapper.TokenMapper;
import com.gongzone.central.point.domain.Point;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.utils.MySqlUtil;
import com.gongzone.central.utils.StatusCode;
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

    @Override
    public List<Member> getAllMembers() {
        return memberMapper.findAll();
    }

    @Override
    public Member getMemberByNo(String memberNo) {
        return memberMapper.info(memberNo);
    }

    @Override
    public Boolean getMemberById(String memberId) {
        return memberMapper.findById(memberId);
    }

    @Override
    public Boolean getMemberByEmail(String memberEmail) {
        return memberMapper.findByEmail(memberEmail);
    }

    public Member getMemberByStatus(String memberNo) {
        return memberMapper.findByStatus(memberNo);
    }

}
