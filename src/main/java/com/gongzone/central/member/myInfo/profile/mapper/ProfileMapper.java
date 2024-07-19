package com.gongzone.central.member.myInfo.profile.mapper;

import com.gongzone.central.file.domain.FileUpload;
import com.gongzone.central.member.myInfo.profile.domain.Profile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProfileMapper {
    void addFile(FileUpload fileUpload);
    void addFileRelation(Profile profile);
    void updateFileRelation(Profile profile);
    Profile getProfile(String memberNo);
}
