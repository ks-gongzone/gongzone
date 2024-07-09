package com.gongzone.central.announce.mapper;

import com.gongzone.central.announce.domain.Announce;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 작성일: 2024-07-02
 * 수정일: 2024-07-03
 * 내용: 쿼리문 페이징 처리(10개의 데이터씩 끊어서 조회)를 위해 총 ROW수 받아오는 메서드 추가
 */
@Mapper
public interface AnnounceMapper {
    // 생성 기능
    void createAnnounce(Announce announce);
    // 조회 기능
    List<Announce> findAllAnnounce(@Param("offset") int offset, @Param("limit") int limit);
    List<Announce> findAnnounceByType(@Param("offset") int offset, @Param("limit") int limit, @Param("type") String type);
    int countAllAnnounce();
    int countAnnounceByType(@Param("type") String type);
    Announce findAnnounceDetail(@Param("announceNo") int announceNo);
    void incrementViews(@Param("announceNo") int announceNo);
    // 수정 기능

    // 삭제 기능

}
