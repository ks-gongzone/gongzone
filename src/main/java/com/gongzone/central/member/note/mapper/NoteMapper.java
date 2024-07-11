package com.gongzone.central.member.note.mapper;

import com.gongzone.central.member.note.domain.Note;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoteMapper {

    Note getNoteByNo(int noteNo);   // 원하는 노트 조회
    List<Note> noteList(String memberNo);      //본인 노트리스트 정보
    void insertNote(Note note);
    void updateReadTimeNote(int noteNo);     // 읽은시간 업데이트
    void updateDeleteNote(int noteNo);    // 사용자 삭제(상태)
}
