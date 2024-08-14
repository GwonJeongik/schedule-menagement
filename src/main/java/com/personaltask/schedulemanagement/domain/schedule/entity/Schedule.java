package com.personaltask.schedulemanagement.domain.schedule.entity;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Schedule {

    private String scheduleId;
    private String schedulePassword;
    private String adminName;
    private String task;
    private String registrationDate;
    private String modificationDate;

    public Schedule(RequestScheduleDto requestScheduleDto) {
        this.scheduleId = requestScheduleDto.getScheduleId();
        this.schedulePassword = requestScheduleDto.getSchedulePassword();
        this.task = requestScheduleDto.getTask();
        this.adminName = requestScheduleDto.getAdminName();
        this.registrationDate = requestScheduleDto.getRegistrationDate();
        this.modificationDate = requestScheduleDto.getModificationDate();
    }
}
