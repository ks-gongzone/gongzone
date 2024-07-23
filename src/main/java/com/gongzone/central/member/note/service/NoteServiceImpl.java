package com.gongzone.central.member.note.service;

import com.gongzone.central.member.note.domain.Note;
import com.gongzone.central.member.note.mapper.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteMapper noteMapper;


    @Override
    public Mono<Note> getNoteByNo(int noteNo) {
        return Mono.fromCallable(() -> noteMapper.getNoteByNo(noteNo))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<Note> getNoteListByMemberNo(String memberNo) {
        return Flux.defer(() -> Flux.fromIterable(noteMapper.noteList(memberNo)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> saveNote(Note note) {
        return Mono.fromRunnable(() -> noteMapper.insertNote(note))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> updateReadTime(int noteNo) {
        return Mono.fromRunnable(() -> noteMapper.updateReadTimeNote(noteNo))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> updateDeleteNote(int noteNo) {
        return Mono.fromRunnable(() -> noteMapper.updateDeleteNote(noteNo))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<Void> updateDeleteNoteTarget(int noteNo) {
        return Mono.fromRunnable(() -> noteMapper.updateDeleteNoteTarget(noteNo))
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }

    @Override
    public Mono<List<Map<String, Object>>> countNewNotes(String memberNo) {
        return Mono.fromCallable(() -> noteMapper.countNewNotes(memberNo))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
