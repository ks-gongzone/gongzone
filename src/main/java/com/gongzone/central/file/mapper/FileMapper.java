package com.gongzone.central.file.mapper;

import com.gongzone.central.file.domain.FileUpload;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    void updateFile(FileUpload fileUpload);
    void addFile(FileUpload fileUpload);
    List<FileUpload> getBoardFileList(int fileNo);
}