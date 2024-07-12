package com.gongzone.central.member.note.controller;

import com.gongzone.central.member.note.domain.Note;
import com.gongzone.central.member.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/note/noteCheck/{noteNo}")
    public Mono<Note> getNoteByNo(@PathVariable int noteNo) {
        return noteService.getNoteByNo(noteNo);
    }

    @GetMapping("/note/noteList/{memberNo}")
    public Flux<Note> noteList(@PathVariable String memberNo) {
        return noteService.getNoteListByMemberNo(memberNo);
    }

    @PostMapping("/note/insertNote")
    public Mono<Void> insertNote(@RequestBody Note note) {
        return noteService.saveNote(note);
    }

    @PostMapping("/note/updateReadTime/{noteNo}")
    public Mono<Void> updateReadTimeNote(@PathVariable int noteNo) {
        return noteService.updateReadTime(noteNo);
    }

    @PostMapping("/note/updateDelete/{noteNo}")
    public Mono<Void> updateDeleteNote(@PathVariable int noteNo) {
        return noteService.updateDeleteNote(noteNo);
    }

    @PostMapping("/note/updateDeleteTarget/{noteNo}")
    public Mono<Void> updateDeleteNoteTarget(@PathVariable int noteNo) {
        return noteService.updateDeleteNoteTarget(noteNo);
    }
}
