package com.gongzone.central.file.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gongzone.central.file.domain.FileUpload;


@Component
public class FileUtil {

    @Value("${files.path}")
    private String fileRealPath;

    public FileUpload parseFileInfo(MultipartFile multipartFile){

        // 파일이 존재하지 않은 경우
        if(ObjectUtils.isEmpty(multipartFile)){
            return null;
        }

        // 날짜 패턴
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 현재 날짜(디렉토리 명)
        ZonedDateTime current = ZonedDateTime.now();
        // 콘텐츠 유형
        //String contentType = multipartFile.getContentType();
        String directory = "attachement/" + current.format(format);

        String path = Paths.get(fileRealPath + directory).toString();

        // 파일 경로 셋팅
        File file = new File(path);

        // 파일 디렉토리 없을 경우 디렉토리생성
        if(file.exists() == false){
            file.mkdirs();
        }

        // 파일 명이 겹치지 않게 파일명 설정
        String resultFileName = "";
        String[] fileNameSplit = multipartFile.getOriginalFilename().split("\\.");

        for(int i=0; i<fileNameSplit.length; i++) {
            if(i == (fileNameSplit.length-1)) {
                fileNameSplit[i] = "." + fileNameSplit[i];
            }else {
                fileNameSplit[i] = fileNameSplit[i].replaceAll("\\s", "") + Long.toString(System.nanoTime());
            }
            resultFileName += fileNameSplit[i];
        }

        // 파일의 업로드 경로 설정
        byte[] bytes;
        Path uploadPath = Paths.get(path + "/" + resultFileName);

        try {

            bytes = multipartFile.getBytes();

            // 파일업로드
            Files.write(uploadPath, bytes);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String fileIdx = "file_"+current.format(format)+Long.toString(System.nanoTime());
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFileIdx(fileIdx);
        fileUpload.setFileSize(multipartFile.getSize());
        fileUpload.setFileOriginalName(multipartFile.getOriginalFilename());
        fileUpload.setFileNewName(resultFileName);

        fileUpload.setFilePath("/api/"+ directory + "/" + resultFileName);

        return fileUpload;
    }
}
