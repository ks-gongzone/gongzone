package com.gongzone.central.scheduler;

import com.gongzone.central.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class CommonScheduler {

    private final BoardMapper boardMapper;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void boardStatusUpdate() {
        boardMapper.updateBoardPeriodStatus();
        boardMapper.updatePartyPeriodStatus();
    }
}