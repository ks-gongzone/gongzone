package com.gongzone.central.announce.service;

import com.gongzone.central.announce.domain.Announce;
import com.gongzone.central.announce.mapper.AnnounceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @작성일: 2024-07-02
 * @수정일: 2024-07-02
 * @내용: 공지사항 페이지 서비스
 */
@Service
@RequiredArgsConstructor
public class AnnounceServiceImpl implements AnnounceService {
    private final AnnounceMapper announceMapper;

    /**
     * @내용: 전체 공지 조회 및 타입 별 조회
     */
    @Override
    public List<Announce> findAllAnnounce(int offset, int limit) {
        return announceMapper.findAllAnnounce(offset, limit);
    }

    @Override
    public List<Announce> findAnnounceByType(int offset, int limit, String type) {
        return announceMapper.findAnnounceByType(offset, limit, type);
    }

    @Override
    public Announce findAnnounceDetail(int announceNo) {
        return announceMapper.findAnnounceDetail(announceNo);
    }

    @Override
    public int countAllAnnounce() {
        return announceMapper.countAllAnnounce();
    }

    @Override
    public void incrementViews(int announceNo) {
        announceMapper.incrementViews(announceNo);
    }

    /**
     * @작성일: 2024-07-08
     * @내용: 공지사항 작성
     */
    @Override
    public void createAnnounce(Announce announce) {
        announceMapper.createAnnounce(announce);
    }

    /**
     * @작성일: 2024-07-09
     * @내용: 공지사항 수정
     */
    @Override
    public void updateAnnounce(Announce announce) {
        announceMapper.updateAnnounce(announce);
    }

    /**
     * @수정일: 2024-07-10
     * @내용: 공지사항 삭제
     */
    @Override
    public void deleteAnnounce(int announceNo) {
        announceMapper.deleteAnnounce(announceNo);
    }

    /**
     * @수정일: 2024-07-03
     * @내용: 타입 확인 후 에러처리
     */
    @Override
    public int countAnnounceByType(String type) {
        return announceMapper.countAnnounceByType(type);
    }
}
