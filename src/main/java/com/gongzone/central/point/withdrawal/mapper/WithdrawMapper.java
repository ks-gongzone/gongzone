package com.gongzone.central.point.withdrawal.mapper;

import com.gongzone.central.point.withdrawal.domain.Withdraw;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WithdrawMapper {
	void insertPointWithdraw(Withdraw withdraw);

}
