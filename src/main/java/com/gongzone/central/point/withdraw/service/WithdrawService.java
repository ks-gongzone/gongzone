package com.gongzone.central.point.withdraw.service;

import com.gongzone.central.point.domain.request.PointDTO;
import com.gongzone.central.point.withdraw.domain.Withdraw;
import java.util.List;

public interface WithdrawService {

	void insert(Withdraw withdraw);

	Withdraw get(String withdrawNo);

	List<Withdraw> getMany(String memberNo, int size, int page);

	void withdraw(String memberNo, PointDTO request);

}
