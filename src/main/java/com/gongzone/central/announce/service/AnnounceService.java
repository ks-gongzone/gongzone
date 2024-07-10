package com.gongzone.central.announce.service;

import com.gongzone.central.announce.domain.Announce;

import java.util.List;

/**
 * @작성일: 2024-07-02
 * @수정일: 2024-07-03
 * @내용: 페이징 처리된 공지사항 조회
 */
public interface AnnounceService {
    // 생성기능
    void createAnnounce(Announce announce);
    // 조회기능
    List<Announce> findAllAnnounce(int offset, int limit);
    List<Announce> findAnnounceByType(int offset, int limit, String type);
    int countAllAnnounce();
    int countAnnounceByType(String type);
    Announce findAnnounceDetail(int announceNo);
    void incrementViews(int announceNo);
    // 수정기능
    void updateAnnounce(Announce announce);
    // 삭제기능
    void deleteAnnounce(int announceNo);
}
