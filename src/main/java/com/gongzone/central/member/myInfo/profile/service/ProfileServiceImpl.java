package com.gongzone.central.member.myInfo.profile.service;

import com.gongzone.central.file.domain.FileUpload;
import com.gongzone.central.file.util.FileUtil;
import com.gongzone.central.member.myInfo.profile.domain.Profile;
import com.gongzone.central.member.myInfo.profile.mapper.ProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        if (fileUpload == null) {
            throw new RuntimeException("파일 파싱 실패");
        }
        // 2. 파일 정보 저장
        profileMapper.addFile(fileUpload);
        // 3. 방금 삽입된 파일의 file_no 가져오기
        int fileNo = fileUpload.getFileIdx();
        // 4. 프로필 사진 정보 저장
        Profile profile = Profile.builder()
                .fileNo(fileNo)
                .fileUsage(memberNo)
                .build();
        profileMapper.addFileRelation(profile);
    }

    @Override
    @Transactional
    public FileUpload updateProfilePicture(String memberNo, MultipartFile file) {
        // 1. 파일 정보 파싱
        FileUpload fileUpload = fileUtil.parseFileInfo(file);
        if (fileUpload == null) {
            throw new IllegalArgumentException("파일 파싱 실패");
        }
        // 2. 파일 정보 저장
        profileMapper.addFile(fileUpload);
        // 3. file_relation 업데이트
        Profile profile = Profile.builder()
                .fileNo(fileUpload.getFileIdx())
                .fileUsage(memberNo)
                .build();
        if (profileMapper.existsFileRelation(memberNo)) {
            profileMapper.updateFileRelation(profile);
        } else {
            profileMapper.addFileRelation(profile);
        }
        return fileUpload; // 변경된 부분: 반환 타입이 void에서 FileUpload로 변경되었습니다.
    }

    // 프로필 카드 조회
    @Override
    public Profile getProfile(String memberNo) {
        Profile profile = profileMapper.getProfile(memberNo);
        if (profile == null) {

        } else {
            // 파일 데이터 출력
            if (profile.getFiles() != null && !profile.getFiles().isEmpty()) {
            }
        }
        return profile;
    }

    // 전체 프로필 카드 가져온다.
    @Override
    public List<Profile> getAllProfiles() {
        List<Profile> profiles = profileMapper.getAllProfiles();
        if (profiles != null && !profiles.isEmpty()) {
            for (Profile profile : profiles) {
                if (profile.getFiles() != null && !profile.getFiles().isEmpty()) {
                    System.out.println("파일: " + profile.getFiles().get(0));
                }
            }
        }
        return profiles;
    }

    // 프로필 편집 시 필요한 파일 파싱 데이터
    public FileUpload parseFile(MultipartFile file) {
        return fileUtil.parseFileInfo(file);
    }
}
