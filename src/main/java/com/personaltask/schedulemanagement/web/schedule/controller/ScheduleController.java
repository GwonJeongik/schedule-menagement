package com.personaltask.schedulemanagement.web.schedule.controller;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.dto.ResponseScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.service.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Objects;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ManagementService<RequestScheduleDto, ResponseScheduleDto> service;

    @Autowired
    public ScheduleController(ManagementService<RequestScheduleDto, ResponseScheduleDto> service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseScheduleDto> saveSchedule(@RequestBody RequestScheduleDto requestScheduleDto) throws SQLException {

        try {
            if (Objects.isNull(requestScheduleDto.getTask())) {
                throw new IllegalArgumentException("스케쥴이 비어있습니다.");
            }
            ResponseScheduleDto saveResponseScheduleDto = service.callSave(requestScheduleDto);

            return new ResponseEntity<>(saveResponseScheduleDto, HttpStatus.OK);

        } catch (RuntimeException e) {
            log.error("error log = ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/find-schedule")
    public ResponseEntity<ResponseScheduleDto> findScheduleById(
            @RequestBody RequestScheduleDto requestScheduleDto
    ) throws SQLException {

        if (requestScheduleDto.getScheduleId().isEmpty()) {
            throw new IllegalArgumentException("아이디가 비어있습니다.");
        }

        if (requestScheduleDto.getScheduleId().length() > 36) {
            throw new IllegalArgumentException("아이디의 최대 길이를 초과하였습니다.");
        }

        ResponseScheduleDto responseScheduleDto = service.callFindById(requestScheduleDto);
        return new ResponseEntity<>(responseScheduleDto, HttpStatus.OK);
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
