package com.gongzone.central.member.service;

import com.gongzone.central.member.domain.Member;

import java.util.List;

public interface MemberService {

    Member findByNumber(String memberNo);

    List<Member> findAll();

    void insert(Member member);

    void update(Member member);

    void delete(Member member);

}
