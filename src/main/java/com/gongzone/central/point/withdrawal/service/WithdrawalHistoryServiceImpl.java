package com.gongzone.central.point.withdrawal.service;

import com.gongzone.central.point.withdrawal.domain.Withdraw;
import com.gongzone.central.point.withdrawal.mapper.WithdrawMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawalHistoryServiceImpl implements WithdrawHistoryService {

	private final WithdrawMapper withdrawMapper;

	@Override
	public void insert(Withdraw withdraw) {
		try {
			withdrawMapper.insertPointWithdraw(withdraw);
		} catch (Exception e) {
			System.err.printf("Exception:\n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
		}
	}

}
