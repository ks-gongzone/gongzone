package com.gongzone.central.point.withdraw.service;

import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.withdraw.domain.Withdraw;
import com.gongzone.central.point.withdraw.mapper.WithdrawMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {

	private final PointMapper pointMapper;
	private final WithdrawMapper withdrawMapper;

	
	@Override
	public void insert(Withdraw withdraw) {
		try {
			withdrawMapper.insert(withdraw);
		} catch (Exception e) {
			System.err.printf("Exception:\n\t%s\n", e);
			System.err.printf("\tCaused by: %s\n", e.getCause() != null ? e.getCause().toString() : "null");
		}
	}

	@Override
	public Withdraw get(String withdrawNo) {
		return withdrawMapper.get(withdrawNo);
	}

	@Override
	public List<Withdraw> getMany(String memberNo, int size, int page) {
		String memberPointNo = pointMapper.getPointNo(memberNo);
		return withdrawMapper.getMany(memberPointNo, size, page - 1);
	}

}
