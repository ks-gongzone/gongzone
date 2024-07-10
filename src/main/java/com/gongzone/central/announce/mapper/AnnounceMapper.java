package com.gongzone.central.announce.mapper;

import com.gongzone.central.announce.domain.Announce;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * 작성일: 2024-07-02
 * 수정일: 2024-07-09
 * 내용: 공지사항 C.R.U.D 맵퍼 인터페이스
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
    void updateAnnounce(Announce announce);
    // 삭제 기능
    void deleteAnnounce(int announceNo);
}
