package com.gongzone.central.member.note.controller;

import com.gongzone.central.member.note.domain.Note;
import com.gongzone.central.member.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/noteCheck/{noteNo}")
    public Note getNoteByNo(@PathVariable int noteNo) {
        return noteService.getNoteByNo(noteNo);
    }

    @GetMapping("/noteList/{memberNo}")
    public List<Note> noteList(@PathVariable String memberNo) {
        return noteService.getNoteListByMemberNo(memberNo);
    }

    @PostMapping("/insertNote")
    public void insertNote(@RequestBody Note note) {
        noteService.saveNote(note);
    }

    @PutMapping("/updateReadTime/{noteNo}")
    public void updateReadTimeNote(@PathVariable int noteNo, @RequestBody Note note) {
        noteService.updateReadTime(noteNo);
    }

    @DeleteMapping("/updateDelete/{noteNo}")
    public void updateDeleteNote(@PathVariable int noteNo) {
        noteService.updateDeleteNote(noteNo);
    }
}
