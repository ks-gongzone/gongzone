package com.gongzone.central.member.myInfo.dropdown.service;

import com.gongzone.central.member.myInfo.dropdown.domain.DropDownInfo;

public interface DropDownService {
  DropDownInfo findByData(String memberNo, String pointNo);
}
