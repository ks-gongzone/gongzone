package com.gongzone.central.point.withdraw.service;

import com.gongzone.central.point.domain.request.PointDTO;
import com.gongzone.central.point.mapper.PointMapper;
import com.gongzone.central.point.service.PointHistoryService;
import com.gongzone.central.point.service.PointService;
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

	private final PointService pointService;
	private final PointHistoryService pointHistoryService;

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
		String memberPointNo = pointMapper.getMemberPointNo(memberNo);
		return withdrawMapper.getMany(memberPointNo, size, page - 1);
	}


	/**
	 * 요청을 기반으로 회원 포인트 인출을 처리한다.
	 *
	 * @param memberNo 회원 번호
	 * @param request  회원 포인트 인출 객체
	 */
	@Override
	public void withdraw(String memberNo, PointDTO request) {
		String memberPointNo = pointMapper.getMemberPointNo(memberNo);

		// 1. 포인트 내역 삽입
		String historyNo = pointHistoryService.insert(memberPointNo, request);

		// TODO: 실제 운영 계좌에서 포인트 출금 처리
		// 1-1. 인출 내역 생성
		Withdraw withdraw = (Withdraw) request.getDetail();
		withdraw.setPointHistoryNo(historyNo);

		// 2. 인출 내역 삽입
		insert(withdraw);
		// TODO: 관리자 인출 처리 필요(관리자 페이지에 구현)
		// NOTE: 인출 상태코드 테이블 변경 필요 -> (인출 대기중, 인출 완료)

		// 3. 포인트 차감
		pointService.updatePoint(memberPointNo, request);

		// 4. 포인트 내역 업데이트(성공)
		pointHistoryService.updateSuccess(historyNo, request);
		
	}

}
