package com.gongzone.central.point.service;


import com.gongzone.central.point.domain.PointHistory;
import com.gongzone.central.point.domain.request.PointChargeRequest;
import com.gongzone.central.point.domain.request.PointRequest;
import com.gongzone.central.point.domain.request.PointWithdrawRequest;
import com.gongzone.central.point.mapper.PointMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

	private final PointTransactionService pointTransactionService;
	private final PointHistoryService pointHistoryService;

	private final PointMapper pointMapper;


	/**
	 * 회원의 (모든) 포인트 사용 내역을 반환한다.
	 *
	 * @param memberNo 회원 번호
	 * @param size     페이지 크기
	 * @param page     페이지 번호
	 * @return 포인트 사용내역 List
	 */
	@Override
	public List<PointHistory> getHistories(String memberNo, int size, int page) {
		return pointMapper.getHistories(getMemberPointNo(memberNo), size, page - 1);
	}

	/**
	 * 회원의 포인트 사용 내역을 반환한다.
	 *
	 * @param memberNo       회원 번호
	 * @param pointHistoryNo 포인트 내역 번호
	 * @return 포인트 사용내역
	 */
	@Override
	public PointHistory getHistory(String memberNo, String pointHistoryNo) {
		return pointMapper.getHistory(getMemberPointNo(memberNo), pointHistoryNo);
	}

	/**
	 * 회원이 현재 보유한 포인트를 반환한다.
	 *
	 * @param memberNo 회원 번호
	 * @return 현재 보유 포인트
	 */
	@Override
	public Integer getCurrentPoint(String memberNo) {
		return pointMapper.getCurrentPoint(getMemberPointNo(memberNo));
	}

	/**
	 * 요청을 기반으로 회원 포인트 충전을 처리한다.
	 *
	 * @param memberNo 회원 번호
	 * @param request  포인트 충전 객체
	 */
	@Override
	public void charge(String memberNo, PointChargeRequest request) {
		pointTransactionService.charge(getMemberPointNo(memberNo), request);
	}

	/**
	 * 요청을 기반으로 회원 포인트 인출을 처리한다.
	 *
	 * @param memberNo 회원 번호
	 * @param request  회원 포인트 인출 객체
	 */
	@Override
	public void withdraw(String memberNo, PointWithdrawRequest request) {
		pointTransactionService.withdraw(getMemberPointNo(memberNo), request);
	}

	/**
	 * 회원 번호를 받아 회원 포인트 번호를 반환한다.
	 *
	 * @param memberNo 회원 번호
	 * @return 회원 포인트 번호
	 */
	private String getMemberPointNo(String memberNo) {
		return pointMapper.getMemberPointNo(memberNo);
	}

	@Override
	public void update(String memberNo, PointRequest request) {
		pointTransactionService.updatePoint(memberNo, request);
	}

	@Override
	public String insertHistory(String memberNo, PointRequest request) {
		return pointHistoryService.insert(getMemberPointNo(memberNo), request);
	}

	@Override
	public void updateHistorySuccess(String historyNo, PointRequest request) {
		pointHistoryService.updateSuccess(historyNo, request);
	}

}
