package com.personaltask.schedulemanagement.domain.schedule.controller;

import com.personaltask.schedulemanagement.domain.schedule.model.Schedule;
import com.personaltask.schedulemanagement.domain.schedule.model.dto.ScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.service.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Objects;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ManagementService<Schedule, ScheduleDto> service;

    @Autowired
    public ScheduleController(ManagementService<Schedule, ScheduleDto> service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<ScheduleDto> saveSchedule(@RequestBody Schedule schedule) throws SQLException {
        try {
            if (Objects.isNull(schedule.getTask())) {
                throw new IllegalArgumentException("스케쥴이 비어있습니다.");
            }
            ScheduleDto saveScheduleDto = service.callSave(schedule);

            return new ResponseEntity<>(saveScheduleDto, HttpStatus.OK);

        } catch (RuntimeException e) {
            log.error("error log={}", e.getMessage());
            return null;
        }
    }

    @GetMapping
    public void findScheduleById(@RequestBody Schedule schedule) throws SQLException {
    }
//
//    @GetMapping
//    public void findAllSchedule() {
//
//    }
//
//    @PostMapping
//    public void updateSchedule() {
//
//    }
//
//    @DeleteMapping
//    public void deleteSchedule() {
//
//    }
}
