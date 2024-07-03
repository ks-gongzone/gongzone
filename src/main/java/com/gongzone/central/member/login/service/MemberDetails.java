package com.gongzone.central.member.login.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.MemberLevel;
import com.gongzone.central.point.domain.Point;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Builder
public class MemberDetails implements UserDetails {

    private final Member member;
    private final Point point;
    private String token;

    public MemberDetails(Member member, Point point) {
        this.member = member;
        this.point = point;
    }

    public MemberDetails(Member member, Point point, String token) {
        this.member = member;
        this.point = point;
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        MemberLevel memberLevel = MemberLevel.fromLevel(member.getMemberLevel());
        return Collections.singletonList(new SimpleGrantedAuthority(memberLevel.getLevelName()));
    }

    @Override
    public String getPassword() {
        return member.getMemberPw();
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정의 만료 여부 설정
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정의 잠금 여부 설정
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명의 만료 여부 설정
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정의 활성화 여부 설정
    }

    public String getMemberNo() {
        return member.getMemberNo();
    }

    public String getPointNo() {
        return point.getMemberPointNo();
    }

    public String getEmail() {
        return member.getMemberEmail();
    }

    public String getMemberId() {
        return member.getMemberId();
    }

    public void setToken(String token) {
        this.token = token;
    }
}
