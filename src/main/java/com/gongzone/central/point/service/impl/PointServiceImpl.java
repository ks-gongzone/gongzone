package com.gongzone.central.point.service.impl;


import com.gongzone.central.point.domain.request.PointRequest;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

	private final PointTransactionService pointTransactionService;

	private final PointMapper pointMapper;


	/**
	 * 회원이 현재 보유한 포인트를 반환한다.
	 *
	 * @param memberNo 회원 번호
	 * @return 현재 보유 포인트
	 */
	@Override
	public Integer getCurrentPoint(String memberNo) {
		return pointMapper.getCurrentPoint(getPointNo(memberNo));
	}

	/**
	 * 요청을 기반으로 회원 포인트 충전을 처리한다.
	 *
	 * @param memberNo 회원 번호
	 * @param request  포인트 충전 객체
	 */
	@Override
	public void charge(String memberNo, PointRequest request) {
		pointTransactionService.charge(getPointNo(memberNo), request);
	}

	/**
	 * 요청을 기반으로 회원 포인트 인출을 처리한다.
	 *
	 * @param memberNo 회원 번호
	 * @param request  회원 포인트 인출 객체
	 */
	@Override
	public void withdraw(String memberNo, PointRequest request) {
		pointTransactionService.withdraw(getPointNo(memberNo), request);
	}

	/**
	 * 회원 번호를 받아 회원 포인트 번호를 반환한다.
	 *
	 * @param memberNo 회원 번호
	 * @return 회원 포인트 번호
	 */
	@Override
	public String getPointNo(String memberNo) {
		return pointMapper.getPointNo(memberNo);
	}

	@Override
	public void update(String memberNo, PointRequest request) {
		pointTransactionService.updatePoint(getPointNo(memberNo), request);
	}

}
