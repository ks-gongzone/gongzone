package com.gongzone.central.file.domain;

import lombok.Data;

@Data
public class FileUpload {
    private String fileIdx;
    private String fileOriginalName;
    private String fileNewName;
    private String filePath;
    private Long fileSize;
}