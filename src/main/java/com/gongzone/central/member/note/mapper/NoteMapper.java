package com.gongzone.central.member.note.mapper;

import com.gongzone.central.member.note.domain.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoteMapper {

    Note getNoteByNo(int noteNo);
    List<Note> noteList(String memberNo);
    void insertNote(Note note);
    void updateReadTimeNote(int noteNo);
    void updateDeleteNote(int noteNo);
    void updateDeleteNoteTarget(int noteNo);
    List<Map<String, Object>> countNewNotes(@Param("memberNo") String memberNo);
}
