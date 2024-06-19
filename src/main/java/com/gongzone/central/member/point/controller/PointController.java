package com.gongzone.central.member.point.controller;

import com.gongzone.central.member.point.domain.PointCharge;
import com.gongzone.central.member.point.domain.PointHistory;
import com.gongzone.central.member.point.service.PointService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class PointController {
    private final PointService pointService;


    public PointController(PointService pointService) {
        this.pointService = pointService;
    }


    /**
     * 회원의 포인트 사용 내역을 응답으로 반환한다.
     *
     * @param memberPointNo
     * @return
     */
    @GetMapping("/{memberPointNo}/point/history")
    public Map<String, List<PointHistory>> getMemberPointHistory(@PathVariable String memberPointNo) {
        Map<String, List<PointHistory>> response = pointService.getAllHistory(memberPointNo);

        return response;
    }

    /**
     * 회원이 현재 보유한 포인트를 응답으로 반환한다.
     *
     * @param memberPointNo
     * @return
     */
    @GetMapping("/{memberPointNo}/point")
    public Map<String, Integer> getMemberPoint(@PathVariable String memberPointNo) {
        Map<String, Integer> response = pointService.getCurrentPoint(memberPointNo);

        return response;
    }

    @PostMapping("/{memberPointNo}/point/charge")
    public Map<String, String> postPointCharge(@PathVariable String memberPointNo,
                                               @RequestBody PointCharge request) {
        Map<String, String> response = pointService.chargeMemberPoint(memberPointNo, request);

        return response;
    }

}
