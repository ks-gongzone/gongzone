package com.gongzone.central.member.login.service;

import com.gongzone.central.member.domain.Member;
import com.gongzone.central.member.domain.MemberLevel;
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

    public MemberDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        MemberLevel memberLevel = MemberLevel.fromLevel(member.getMemberLevel());
        return Collections.singletonList(new SimpleGrantedAuthority(memberLevel.getLevelName()));
    }

    @Override
    public String getPassword() {
        return member.getMemberPw(); // Member 클래스의 비밀번호 필드
    }

    @Override
    public String getUsername() {
        return member.getMemberId(); // Member 클래스의 사용자 이름(ID) 필드
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
}
