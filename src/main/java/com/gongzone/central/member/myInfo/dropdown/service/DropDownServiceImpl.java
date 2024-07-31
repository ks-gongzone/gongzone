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
        return dropDownMapper.findByData(memberNo, pointNo);
    }
}
