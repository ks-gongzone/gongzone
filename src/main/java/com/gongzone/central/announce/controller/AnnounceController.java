package com.gongzone.central.announce.controller;

import com.gongzone.central.announce.domain.Announce;
import com.gongzone.central.announce.service.AnnouceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @작성일: 2024-07-02
 * @수정일: 2024-07-02
 * @내용: 공지사항 조회 컨트롤러
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/announce")
public class AnnounceController {
    private final AnnouceService annouceService;

    /**
     * @작성일: 2024-07-03
     * @내용: 변수명 통일을 위한 타입코드 설정
     */
    private final String getType(String typeCode) {
        switch (typeCode) {
            case "T020101":
                return "공지";
            case "T020102":
                return "FAQ";
            case "T020103":
                return "프로모션";
            default:
                return "존재하지 않는 코드.";
        }
    }

    /**
     * @작성일: 2024-07-02
     * @수정일: 2024-07-03
     * @내용: 전체 공지사항 조회
     * @수정내용: 백엔드 통신하는 변수명 통일
     */
    @GetMapping
    public Map<String, Object> findAllAnnounce(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {

        int offset = (page - 1) * size;
        List<Announce> announces;
        int totalCount;

        if(type == null || type.isEmpty()) {
            announces = annouceService.findAllAnnounce(offset, size);
            totalCount = annouceService.countAllAnnounce();
        } else {
            announces = annouceService.findAnnounceByType(offset, size, type);
            totalCount = annouceService.countAnnounceByType(type);
        }

        System.out.println("조회 공지사항 목록" + announces);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Announce announce : announces) {
            Map<String, Object> map = new HashMap<>();

            map.put("type", announce.getTypeCodeDes());
            map.put("title", announce.getAnnounceTitle());
            map.put("date", announce.getAnnounceDate());
            map.put("views", announce.getViewCount());
            result.add(map);
        }

        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> response = new HashMap<>();
        response.put("announcements", result);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);

        return response;
    }
}
