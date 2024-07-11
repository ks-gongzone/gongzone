package com.gongzone.central.member.note.service;

import com.gongzone.central.member.note.domain.Note;
import com.gongzone.central.member.note.mapper.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteMapper noteMapper;


    @Override
    public Note getNoteByNo(int noteNo) {
        return noteMapper.getNoteByNo(noteNo);
    }

    @Override
    public List<Note> getNoteListByMemberNo(String memberNo) {
        return noteMapper.noteList(memberNo);
    }

    @Override
    public void saveNote(Note note) {
        noteMapper.insertNote(note);
    }

    @Override
    public void updateReadTime(int noteNo) {
        noteMapper.updateReadTimeNote(noteNo);
    }

    @Override
    public void updateDeleteNote(int noteNo) {
        noteMapper.updateDeleteNote(noteNo);
    }
}
