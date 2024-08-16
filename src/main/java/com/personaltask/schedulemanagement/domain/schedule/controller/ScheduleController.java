package com.personaltask.schedulemanagement.domain.schedule.controller;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.dto.ResponseScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

    // **요청과 응답 controller 역할**
    // 할일, 담당자명, 비밀번호, 작성/수정일
    @PostMapping("/add")
    public ResponseEntity<ResponseScheduleDto> saveSchedule(@RequestBody RequestScheduleDto requestScheduleDto) {

        try {
            // 저장 수행 로직(컨트롤러)
            ResponseScheduleDto responseScheduleDto = service.callSave(requestScheduleDto);
            return new ResponseEntity<>(responseScheduleDto, HttpStatus.OK);

        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException | IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get -> @RequestBody X
    // 고유 식별자만 받음
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ResponseScheduleDto> findScheduleById(@PathVariable int scheduleId) {

        try {
            ResponseScheduleDto responseScheduleDto = service.callFindById(scheduleId);
            return new ResponseEntity<>(responseScheduleDto, HttpStatus.OK);

        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<ResponseScheduleDto>> findAllSchedule(
            @ModelAttribute RequestScheduleDto requestScheduleDto
    ) {
        try {
            List<ResponseScheduleDto> responseScheduleDtos = service.callFindAll(requestScheduleDto);
            return new ResponseEntity<>(responseScheduleDtos, HttpStatus.OK);

        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 할 일 담당자명
    // patch vs put
    @PatchMapping("/{scheduleId}/edit")
    public ResponseEntity<ResponseScheduleDto> updateSchedule(
            @PathVariable int scheduleId,
            @RequestBody RequestScheduleDto requestScheduleDto
    ) {
        try {
            ResponseScheduleDto responseScheduleDto = service.callUpdate(scheduleId, requestScheduleDto);
            return new ResponseEntity<>(responseScheduleDto, HttpStatus.OK);

        } catch (SQLException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{scheduleId}/delete")
    public ResponseEntity<Object> deleteSchedule(
            @PathVariable int scheduleId,
            @RequestBody RequestScheduleDto requestScheduleDto
    ) {

        try {
            log.debug("controller schedule={}", requestScheduleDto);

            service.callDelete(scheduleId, requestScheduleDto);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
