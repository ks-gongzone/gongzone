package com.gongzone.central.member.mapper;

import com.gongzone.central.member.domain.Token;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TokenMapper {
    void insert(Token token);

    List<Token> findAll();

    Token findByMemberNo(String memberNo);

    void update(Token token);

    void delete(int tokenNo);
}
