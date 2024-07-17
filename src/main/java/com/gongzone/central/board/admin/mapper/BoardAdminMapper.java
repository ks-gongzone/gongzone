package com.gongzone.central.board.admin.mapper;

import com.gongzone.central.board.admin.domain.BoardProgress;
import com.gongzone.central.board.admin.domain.BoardWriteDate;
import com.gongzone.central.board.admin.domain.BoardWriteMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardAdminMapper {
    List<BoardProgress> getBoardProgress();
    List<BoardWriteMember> getBoardWriteMember();
    List<BoardWriteDate> getBoardWriteDate();
}
