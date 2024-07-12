package com.gongzone.central.member.note.mapper;

import com.gongzone.central.member.note.domain.Note;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoteMapper {

    Note getNoteByNo(int noteNo);
    List<Note> noteList(String memberNo);
    void insertNote(Note note);
    void updateReadTimeNote(int noteNo);
    void updateDeleteNote(int noteNo);
    void updateDeleteNoteTarget(int noteNo);
}
