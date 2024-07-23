package com.gongzone.central.member.myInfo.profile.service;

import com.gongzone.central.file.domain.FileUpload;
import com.gongzone.central.member.myInfo.profile.domain.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ProfileService {
    Profile getProfile(String memberNo);
    void addProfilePicture(String memberNo, MultipartFile file);
    // 프로필 수정
    FileUpload updateProfilePicture(String memberNo, MultipartFile file);
    // 전체 회원의 프로필사진 조회
    List<Profile> getAllProfiles();
}
