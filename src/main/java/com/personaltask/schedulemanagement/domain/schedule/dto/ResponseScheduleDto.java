package com.personaltask.schedulemanagement.domain.schedule.dto;

import com.personaltask.schedulemanagement.domain.schedule.entity.Schedule;
import lombok.Data;

@Data
public class ResponseScheduleDto {

    private String scheduleId;
    private String adminName; // 담당자 이름
    private String task; // 할 일
    private String registrationDate; // 작성일
    private String modificationDate; // 수정일

    public ResponseScheduleDto(Schedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.task = schedule.getTask();
        this.adminName = schedule.getAdminName();
        this.registrationDate = schedule.getRegistrationDate();
        this.modificationDate = schedule.getModificationDate();
    }
}
