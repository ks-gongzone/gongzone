package com.gongzone.central.member.myInfo.profile.mapper;

import com.gongzone.central.file.domain.FileUpload;
import com.gongzone.central.member.myInfo.profile.domain.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfileMapper {
    void addFile(FileUpload fileUpload);
    void addFileRelation(Profile profile);
    void updateFileRelation(Profile profile);
    boolean existsFileRelation(String fileUsage);
    Profile getProfile(@Param("memberNo") String memberNo);
    List<Profile> getAllProfiles();
}