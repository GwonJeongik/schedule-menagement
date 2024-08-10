package com.personaltask.schedulemanagement.domain.schedule.model;

import lombok.Data;

import java.util.Date;

@Data
public class Schedule {

    private String scheduleId;
    private String schedulePassword; // 비밀번호
    private String task; // 할 일
    private String adminName; // 담당자 이름
    private String registrationDate; // 작성일
    private String modificationDate; // 수정일

    public Schedule() {
    }

    public Schedule(String schedulePassword, String task, String adminName) {
        this.schedulePassword = schedulePassword;
        this.task = task;
        this.adminName = adminName;
    }
}
