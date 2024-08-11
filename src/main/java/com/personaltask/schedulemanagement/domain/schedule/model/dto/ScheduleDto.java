package com.personaltask.schedulemanagement.domain.schedule.model.dto;

import com.personaltask.schedulemanagement.domain.schedule.model.Schedule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDto {

    private String scheduleId;
    private String task; // 할 일
    private String adminName; // 담당자 이름
    private String registrationDate; // 작성일
    private String modificationDate; // 수정일

    public ScheduleDto(Schedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.task = schedule.getTask();
        this.adminName = schedule.getAdminName();
        this.registrationDate = schedule.getRegistrationDate();
        this.modificationDate = schedule.getModificationDate();
    }
}
