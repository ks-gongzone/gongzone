package com.gongzone.central.announce.controller;

import com.gongzone.central.announce.domain.Announce;
import com.gongzone.central.announce.service.AnnounceService;
import com.gongzone.central.member.login.security.JwtUtil;
import com.gongzone.central.member.login.service.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final JwtUtil jwtUtil;

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

        int offset = (page - 1) * size;
        List<Announce> announces;
        int totalCount;

        if (type == null || type.isEmpty()) {
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
        announceService.incrementViews(announceNo);
    }

    @GetMapping("/announce/detail/{announceNo}")
    public Announce findAnnounceDetail(@PathVariable int announceNo) {
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
        if (typeCode == null) {
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
        return ResponseEntity.ok(response);
    }

    /**
     * @수정일: 2024-07-09
     * @내용: 공지사항 수정
     * @참고: (한 번의 요청만 받을 수 있게 putmapping사용)
     */
    @PutMapping("/_admin/announce/update/{announceNo}")
    public ResponseEntity<Map<String, String>> updateAnnounce(
            @PathVariable("announceNo") int announceNo,
            @RequestBody Announce announce,
            Authentication authentication) {

        Map<String, String> response = new HashMap<>();

        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String memberNo = jwtUtil.extractMemberNo(token);

        Announce existAnnounce = announceService.findAnnounceDetail(announceNo);

        if (existAnnounce == null) {
            response.put("error", "공지사항이 존재하지 않습니다.");
        }
        if (!memberNo.equals("M000001")) {
            response.put("error", "관리자가 아닙니다.");
            return ResponseEntity.status(403).body(response);
        }
        String typeCodeDes = announce.getTypeCodeDes();
        String typeCode = (typeCodeDes != null) ? getTypeCode(typeCodeDes) : existAnnounce.getTypeCode();

        // 수정 내용이 null일 시 기존 값 업데이트
        if (announce.getAnnounceTitle() != null) {
            existAnnounce.setAnnounceTitle(announce.getAnnounceTitle());
        }
        if (announce.getAnnounceBody() != null) {
            existAnnounce.setAnnounceBody(announce.getAnnounceBody());
        }
        existAnnounce.setTypeCode(typeCode);
        existAnnounce.setAnnounceNo(announceNo);

        announceService.updateAnnounce(existAnnounce);
        response.put("success", "공지사항 수정완료");
        return ResponseEntity.ok(response);
    }

    /**
     * @수정일: 2024-07-10
     * @내용: 공지사항 삭제
     */
    @DeleteMapping("/_admin/announce/delete/{announceNo}")
    public ResponseEntity<Map<String, String>> deleteAnnounce(
            @PathVariable("announceNo") int announceNo,
            Authentication authentication) {
        Map<String, String> response = new HashMap<>();

        String token = ((MemberDetails) authentication.getPrincipal()).getToken();
        String memberNo = jwtUtil.extractMemberNo(token);
        Announce existAnnounce = announceService.findAnnounceDetail(announceNo);

        if (!memberNo.equals("M000001")) {
            response.put("error", "[백앤드] 관리자가 아닙니다.");
            return ResponseEntity.status(403).body(response);
        }
        if (existAnnounce == null) {
            response.put("error", "[백앤드] 공지사항이 없습니다.");
            return ResponseEntity.status(404).body(response);
        }

        announceService.deleteAnnounce(announceNo);
        response.put("success", "공지사항 삭제완료");
        return ResponseEntity.ok(response);
    }
}
