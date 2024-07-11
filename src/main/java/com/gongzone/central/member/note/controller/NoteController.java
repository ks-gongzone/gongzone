package com.gongzone.central.member.note.controller;

import com.gongzone.central.member.note.domain.Note;
import com.gongzone.central.member.note.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/noteCheck/{noteNo}")
    public Mono<Note> getNoteByNo(@PathVariable int noteNo) {
        return noteService.getNoteByNo(noteNo);
    }

    @GetMapping("/noteList/{memberNo}")
    public Flux<Note> noteList(@PathVariable String memberNo) {
        return noteService.getNoteListByMemberNo(memberNo);
    }

    @PostMapping("/insertNote")
    public Mono<Void> insertNote(@RequestBody Note note) {
        return noteService.saveNote(note);
    }

    @PutMapping("/updateReadTime/{noteNo}")
    public Mono<Void> updateReadTimeNote(@PathVariable int noteNo) {
        return noteService.updateReadTime(noteNo);
    }

    @DeleteMapping("/updateDelete/{noteNo}")
    public Mono<Void> updateDeleteNote(@PathVariable int noteNo) {
        return noteService.updateDeleteNote(noteNo);
    }
}
