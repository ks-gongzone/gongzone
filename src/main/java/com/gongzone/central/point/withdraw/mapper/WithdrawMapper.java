package com.gongzone.central.point.withdraw.mapper;

import com.gongzone.central.point.withdraw.domain.Withdraw;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WithdrawMapper {

	void insert(Withdraw withdraw);

	Withdraw get(String withdrawNo);

	List<Withdraw> getMany(@Param("memberPointNo") String memberPointNo,
						   @Param("size") int size,
						   @Param("page") int page);

}
