package com.gongzone.central.announce.controller;

import com.gongzone.central.announce.domain.Announce;
import com.gongzone.central.announce.service.AnnounceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @작성일: 2024-07-02
 * @수정일: 2024-07-02
 * @내용: 공지사항 조회 컨트롤러
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class AnnounceController {
    private final AnnounceService announceService;
    /**
     * @작성일: 2024-07-03
     * @내용: 변수명 통일을 위한 타입코드 설정
     */
    private final String getTypeCode(String typeCodeDes) {
        switch (typeCodeDes) {
            case "공지":
                return "T020101";
            case "FAQ":
                return "T020102";
            case "프로모션":
                return "T020103";
            default:
                return null;
        }
    }
    /**
     * @작성일: 2024-07-02
     * @수정일: 2024-07-08
     * @내용: 전체 공지사항 조회
     * @수정내용: 상세조회를 위해 announceNo값을 받아옴
     */
    @GetMapping("/announce")
    public Map<String, Object> findAllAnnounce(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {
        System.out.println("[컨트롤러] 공지 전체 조회");

        int offset = (page - 1) * size;
        List<Announce> announces;
        int totalCount;

        if(type == null || type.isEmpty()) {
            announces = announceService.findAllAnnounce(offset, size);
            totalCount = announceService.countAllAnnounce();
        } else {
            announces = announceService.findAnnounceByType(offset, size, type);
            totalCount = announceService.countAnnounceByType(type);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Announce announce : announces) {
            Map<String, Object> map = new HashMap<>();

            map.put("announceNo", announce.getAnnounceNo());
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
    /**
     * @내용: 조회 수 증가
     */
    @PostMapping("/announce/{announceNo}/increment")
    public void incrementViews(@PathVariable int announceNo) {
        System.out.println("증가하는 메서드" + announceNo);
        announceService.incrementViews(announceNo);
    }

    @GetMapping("/announce/detail/{announceNo}")
    public Announce findAnnounceDetail(@PathVariable int announceNo){
        System.out.println("공지상세 글 조회" + announceNo);
        return announceService.findAnnounceDetail(announceNo);
    }
    /**
     * @수정일: 2024-07-09
     * @내용: 공지사항 작성
     */
    @PostMapping("/_admin/announce/write")
    public ResponseEntity<Map<String, String>> createAnnounce(@RequestBody Announce announce) {
        Map<String, String> response = new HashMap<>();

        String memberNo = announce.getMemberNo();
        String announceTitle = announce.getMemberNo();
        String announceBody = announce.getAnnounceBody();
        String typeCodeDes = announce.getTypeCodeDes();
        String typeCode = null;
        if (typeCodeDes != null && !typeCodeDes.isEmpty()) {
            typeCode = getTypeCode(typeCodeDes);
        }
        if (!memberNo.equals("M000001")) {
            response.put("error", "관리자가 아닙니다.");
            return ResponseEntity.status(403).body(response);
        }
        if (announceTitle == null) {
            response.put("error", "필수 항목 누락: Title");
            return ResponseEntity.status(400).body(response);
        }
        if (announceBody == null) {
            response.put("error", "필수 항목 누락: Body");
            return ResponseEntity.status(400).body(response);
        }
        if (typeCode == null || typeCodeDes == null) {
            response.put("error", "필수 항목 누락: typeCode");
            return ResponseEntity.status(400).body(response);
        }
        if (typeCodeDes.equals("존재하지 않는 코드")) {
            response.put("error", "해당코드가 없습니다.");
            return ResponseEntity.status(400).body(response);
        }
        announce.setTypeCode(typeCode);
        announceService.createAnnounce(announce);

        response.put("message", "공지 작성 성공");
        System.out.println("[컨트롤러] 공지사항 내용: " + announceTitle);
        return ResponseEntity.ok(response);
    }




}
