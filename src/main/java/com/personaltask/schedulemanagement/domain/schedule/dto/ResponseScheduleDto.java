package com.personaltask.schedulemanagement.domain.schedule.dto;

import lombok.Data;

@Data
public class ResponseScheduleDto {

    private String scheduleId;
    private String task; // 할 일
    private String adminName; // 담당자 이름
    private String registrationDate; // 작성일
    private String modificationDate; // 수정일

    public ResponseScheduleDto() {
    }

    public ResponseScheduleDto(RequestScheduleDto requestScheduleDto) {
        this.scheduleId = requestScheduleDto.getScheduleId();
        this.task = requestScheduleDto.getTask();
        this.adminName = requestScheduleDto.getAdminName();
        this.registrationDate = requestScheduleDto.getRegistrationDate();
        this.modificationDate = requestScheduleDto.getModificationDate();
    }
}
