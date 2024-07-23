package com.gongzone.central.member.alertSSE.mapper;

import com.gongzone.central.member.alertSSE.domain.AlertAllow;
import com.gongzone.central.member.alertSSE.domain.AlertSSE;
import com.gongzone.central.member.note.domain.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface AlertSSEMapper {

    AlertSSE getAlertSSEByNo(int alertSSENo);
    List<AlertSSE> alertSSEList(String memberNo);
    void insertAlertSSE(AlertSSE alertSSE);
    void updateReadTimeAlertSSE(int noteNo);
    void updateDeleteAlertSSE(int noteNo);
    AlertAllow getAlertAllow(String memberNo);
    List<Map<String, Object>> countNewAlerts(String memberNo);
}
