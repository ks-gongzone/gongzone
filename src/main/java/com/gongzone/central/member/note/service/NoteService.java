package com.gongzone.central.member.note.service;

import com.gongzone.central.member.note.domain.Note;

import java.util.List;

public interface NoteService {

    Note getNoteByNo(int noteNo);
    List<Note> getNoteListByMemberNo(String memberNo);
    void saveNote(Note note);
    void updateReadTime(int noteNo);
    void updateDeleteNote(int noteNo);
}
