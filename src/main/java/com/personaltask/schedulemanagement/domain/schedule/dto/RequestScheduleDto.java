package com.personaltask.schedulemanagement.domain.schedule.dto;

import lombok.Data;

@Data
public class RequestScheduleDto {

    private String scheduleId;
    private String schedulePassword; // 비밀번호
    private String task; // 할 일
    private String adminName; // 담당자 이름
    private String registrationDate; // 작성일
    private String modificationDate; // 수정일

}
