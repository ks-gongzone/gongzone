package com.gongzone.central.file.mapper;

import com.gongzone.central.file.domain.FileUpload;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    void addFile(FileUpload fileUpload);
}