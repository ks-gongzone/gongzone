package com.gongzone.central.member.alertSSE.mapper;

import com.gongzone.central.member.alertSSE.domain.AlertAllow;
import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import com.gongzone.central.member.note.domain.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertSSEMapper {

    AlertSSE getAlertSSEByNo(int alertSSENo);
    List<AlertSSE> alertSSEList(String memberNo);
    void insertAlertSSE(AlertSSE alertSSE);
    void updateReadTimeAlertSSE(int noteNo);
    void updateDeleteAlertSSE(int noteNo);
    AlertAllow getAlertAllow(String memberNo);
}
