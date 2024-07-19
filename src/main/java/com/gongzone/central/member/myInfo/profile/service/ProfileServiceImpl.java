package com.gongzone.central.member.myInfo.profile.service;

import com.gongzone.central.file.domain.FileUpload;
import com.gongzone.central.file.util.FileUtil;
import com.gongzone.central.member.myInfo.profile.domain.Profile;
import com.gongzone.central.member.myInfo.profile.mapper.ProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileMapper profileMapper;
    private final FileUtil fileUtil;

    // 프로필 업로드
    @Override
    @Transactional
    public void addProfilePicture(String memberNo, MultipartFile file) {
        // 1. 파일 정보 파싱
        FileUpload fileUpload = fileUtil.parseFileInfo(file);
        // 2. 파일 정보 저장
        profileMapper.addFile(fileUpload);
        // 3. 프로필 사진 정보 저장
        Profile profile = Profile.builder()
                .fileNo(fileUpload.getFileIdx())
                .fileUsage(memberNo)
                .build();
        profileMapper.addFileRelation(profile);
    }

    @Override
    @Transactional
    public void updateProfilePicture(String memberNo, MultipartFile file) {
        // 1. 파일 정보 파싱
        FileUpload fileUpload = fileUtil.parseFileInfo(file);
        // 2. 파일 정보 저장
        profileMapper.addFile(fileUpload);
        // 3. file_relation 업데이트
        Profile profile = Profile.builder()
                .fileNo(fileUpload.getFileIdx())
                .build();

        profileMapper.updateFileRelation(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public Profile getProfile(String memberNo) {
        return profileMapper.getProfile(memberNo);
    }

}


