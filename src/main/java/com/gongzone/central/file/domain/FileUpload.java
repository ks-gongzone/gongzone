package com.gongzone.central.file.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileUpload {
    private int fileIdx;
    private String fileOriginalName;
    private String fileNewName;
    private String filePath;
    private Long fileSize;
    private LocalDateTime fileDate;
}