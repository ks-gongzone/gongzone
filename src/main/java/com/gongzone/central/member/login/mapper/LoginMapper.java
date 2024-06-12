package com.gongzone.central.member.login.mapper;

import com.gongzone.central.member.domain.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    Member getMemberFromId(String memberId);
}
