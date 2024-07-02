package com.gongzone.central.withdrawal.mapper;

import com.gongzone.central.withdrawal.domain.Withdraw;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WithdrawMapper {
	void insertPointWithdraw(Withdraw withdraw);
	
}
