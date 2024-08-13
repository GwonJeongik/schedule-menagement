package com.personaltask.schedulemanagement.domain.schedule.controller;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.dto.ResponseScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    // Get -> @RequestBody X
    @GetMapping
    public ResponseEntity<List<ResponseScheduleDto>> findAllSchedule(
            @RequestParam String modificationDate,
            @RequestParam String adminName
    ) {

        try {
            List<ResponseScheduleDto> responseScheduleDtos = service.callFindAll();

            return new ResponseEntity<>(responseScheduleDtos, HttpStatus.OK);

        } catch (RuntimeException e) {
            log.error("error log = ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            log.error("error log = ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // **요청과 응답 controller 역할**
    @PostMapping()
    public ResponseEntity<ResponseScheduleDto> saveSchedule(@RequestBody RequestScheduleDto requestScheduleDto) {

            ResponseScheduleDto saveResponseScheduleDto = service.callSave(requestScheduleDto);

            return new ResponseEntity<>(saveResponseScheduleDto, HttpStatus.OK);

    }

    // 고유 식별자만 받음
    @GetMapping("/find-schedule")
    public ResponseEntity<ResponseScheduleDto> findScheduleById(
            @RequestBody RequestScheduleDto requestScheduleDto
    ) {

        try {
            if (requestScheduleDto.getScheduleId().isEmpty()) {
                throw new IllegalArgumentException("아이디가 비어있습니다.");
            }

            if (requestScheduleDto.getScheduleId().length() > 36) {
                throw new StringIndexOutOfBoundsException("아이디의 최대 길이를 초과하였습니다.");
            }

            ResponseScheduleDto responseScheduleDto = service.callFindById(requestScheduleDto);
            return new ResponseEntity<>(responseScheduleDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("error log = ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 할 일 담당자명
    // patch vs put
    // api 명세서 patch 사용 이유
    @PatchMapping("/update-schedule")
    public ResponseEntity<ResponseScheduleDto> updateSchedule(
            @RequestBody RequestScheduleDto requestScheduleDto
    ) throws SQLException {

        try {
            if (requestScheduleDto.getTask().isEmpty() && requestScheduleDto.getAdminName().isEmpty()) {
                throw new IllegalArgumentException("변경할 할 일 내용과 담당자명중 하나는 필요합니다.");
            }

            ResponseScheduleDto responseScheduleDto = service.callUpdate(requestScheduleDto);

            return new ResponseEntity<>(responseScheduleDto, HttpStatus.OK);

        } catch (RuntimeException e) {
            log.error("error log = ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    @DeleteMapping
//    public void deleteSchedule() {
//
//    }
}
