package com.gongzone.central.member.myInfo.dropdown.service;

import com.gongzone.central.member.myInfo.dropdown.domain.DropDownInfo;
import com.gongzone.central.member.myInfo.dropdown.mapper.DropDownMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DropDownServiceImpl implements DropDownService {
    private final DropDownMapper dropDownMapper;

    @Override
    public DropDownInfo findByData(String memberNo, String pointNo) {
        System.out.println("[서비스] 드롭다운 데이터 로드" + memberNo);
        return dropDownMapper.findByData(memberNo, pointNo);
    }
}
