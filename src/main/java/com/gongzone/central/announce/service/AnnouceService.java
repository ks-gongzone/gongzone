package com.gongzone.central.announce.service;

import com.gongzone.central.announce.domain.Announce;

import java.util.List;

/**
 * @작성일: 2024-07-02
 * @수정일: 2024-07-03
 * @내용: 페이징 처리된 공지사항 조회
 */
public interface AnnouceService {
    // 리액트를 사용해서 전체 컴포넌트 클릭시 모든 findAllAnnounce매퍼에서 실행
    // 예외사항 memberNo가 없어도 공지사항은 페이지는 접속 가능
    // 조회 데이터가 10개 이상일 때, 나눠서  1~n번페이징 할 수 있어야 함
    // 프론트 tabItem의 객체와 같은 id를 사용하면 좋지만 영어를 사용해야하면 영어사용
    List<Announce> findAllAnnounce(int offset, int limit);
    List<Announce> findAnnounceByType(int offset, int limit, String type);
    int countAllAnnounce();
    int countAnnounceByType(String type);
}
