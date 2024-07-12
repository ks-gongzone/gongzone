package com.gongzone.central.member.note.service;

import com.gongzone.central.member.note.domain.Note;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NoteService {

    Mono<Note> getNoteByNo(int noteNo);
    Flux<Note> getNoteListByMemberNo(String memberNo);
    Mono<Void> saveNote(Note note);
    Mono<Void> updateReadTime(int noteNo);
    Mono<Void> updateDeleteNote(int noteNo);
    Mono<Void> updateDeleteNoteTarget(int noteNo);
}
