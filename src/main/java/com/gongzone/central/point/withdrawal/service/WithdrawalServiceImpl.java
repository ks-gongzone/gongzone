package com.gongzone.central.point.withdrawal.service;

import com.gongzone.central.point.withdrawal.domain.Withdraw;
import com.gongzone.central.point.withdrawal.mapper.WithdrawMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {
	private final WithdrawMapper withdrawMapper;

	@Override
	public void insertPointWithdraw(Withdraw withdraw) {
		try {
			withdrawMapper.insertPointWithdraw(withdraw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
