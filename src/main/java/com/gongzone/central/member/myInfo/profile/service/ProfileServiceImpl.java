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
        if (fileUpload == null) {
            throw new RuntimeException("파일 파싱 실패");
        }
        System.out.println("[서비스] 파일 정보 파싱 완료: " + fileUpload);
        // 2. 파일 정보 저장
        profileMapper.addFile(fileUpload);
        System.out.println("[서비스] 파일 정보 저장 완료: " + fileUpload);
        // 3. 방금 삽입된 파일의 file_no 가져오기
        int fileNo = fileUpload.getFileIdx();
        System.out.println("삽입된 파일번호: " + fileNo);
        // 4. 프로필 사진 정보 저장
        Profile profile = Profile.builder()
                .fileNo(fileNo)
                .fileUsage(memberNo)
                .build();
        profileMapper.addFileRelation(profile);
        System.out.println("[서비스] 파일 업로드 성공: " + memberNo);
    }

    // 변경된 부분 주석 추가
    @Override
    @Transactional
    public FileUpload updateProfilePicture(String memberNo, MultipartFile file) {
        // 1. 파일 정보 파싱
        FileUpload fileUpload = fileUtil.parseFileInfo(file);
        if (fileUpload == null) {
            throw new IllegalArgumentException("파일 파싱 실패");
        }
        System.out.println("[서비스] 파일 정보 파싱 완료: " + fileUpload); // 1. 콘솔 출력
        // 2. 파일 정보 저장
        profileMapper.addFile(fileUpload);
        System.out.println("[서비스] 파일 정보 저장 완료: " + fileUpload); // 2. 콘솔 출력
        // 3. file_relation 업데이트
        Profile profile = Profile.builder()
                .fileNo(fileUpload.getFileIdx())
                .fileUsage(memberNo)
                .build();
        if (profileMapper.existsFileRelation(memberNo)) {
            profileMapper.updateFileRelation(profile);
            System.out.println("[서비스] 파일 릴레이션 업데이트 완료: " + profile); // 3. 콘솔 출력
        } else {
            profileMapper.addFileRelation(profile);
            System.out.println("[서비스] 파일 릴레이션 추가 완료: " + profile); // 4. 콘솔 출력
        }
        return fileUpload; // 변경된 부분: 반환 타입이 void에서 FileUpload로 변경되었습니다.
    }

    // 프로필 카드 조회
    @Override
    public Profile getProfile(String memberNo) {
        Profile profile = profileMapper.getProfile(memberNo);
        if (profile == null) {
            System.out.println("프로필 조회 결과: " + profile);
        } else {
            System.out.println("[서비스 시작] 회원 번호: " + memberNo);
            System.out.println("[서비스]프로필 정보: " + profile);
            System.out.println("작성 글 수: " + profile.getBoardCount());
            System.out.println("팔로워 수: " + profile.getFollower());
            System.out.println("팔로잉 수: " + profile.getFollowing());

            // 파일 데이터 출력
            if (profile.getFiles() != null && !profile.getFiles().isEmpty()) {
                System.out.println("파일: " + profile.getFiles().get(0));
            }
        }
        return profile;
    }
}
