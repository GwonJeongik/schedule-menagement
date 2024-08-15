package com.personaltask.schedulemanagement.domain.schedule.service;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.dto.ResponseScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.entity.Schedule;
import com.personaltask.schedulemanagement.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;



// 제네릭은 좀 무겁다.
// 에러 처리 컨트롤러 vs 서비스
// GetMapping -> body를 사용 x
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository repository;

    public ResponseScheduleDto callSave(RequestScheduleDto requestScheduleDto) throws SQLException, IOException {

        //비밀번호가 비어있으면 X
        if (requestScheduleDto.getSchedulePassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호 입력 오류");
        }

        //할 일이 비어있으면 X
        if (requestScheduleDto.getTask().isEmpty()) {
            throw new IllegalArgumentException("할 일 입력 오류");
        }

        //이름이 비어있으면 X
        if (requestScheduleDto.getAdminName().isEmpty()) {
            throw new IllegalArgumentException("담당자명 입력 오류");
        }

        // try catch -> 예외 래핑 -> 처음 발생했던 예외가 감싸져서 디버깅이 힘듦.
        int saveScheduleId = repository.save(requestScheduleDto);

        ResponseScheduleDto responseScheduleDto = callFindById(saveScheduleId);
        log.info("saveResponseDto={}", responseScheduleDto.getScheduleId());
        return responseScheduleDto;
    }

    public ResponseScheduleDto callFindById(Integer scheduleId) throws SQLException {

        if (Objects.isNull(scheduleId)) {
            throw new IllegalArgumentException("아이디 입력 필요");
        }

        Schedule findSchedule = repository.findById(scheduleId);

        return new ResponseScheduleDto(findSchedule);
    }

    public List<ResponseScheduleDto> callFindAll(RequestScheduleDto requestScheduleDto) throws SQLException {

        List<Schedule> schedules = repository.findAll(requestScheduleDto);

        List<ResponseScheduleDto> list = schedules.stream().map(ResponseScheduleDto::new).toList();
        return list;
    }

    public void callUpdate(RequestScheduleDto requestScheduleDto) throws SQLException {

        log.debug("service schedule={}", requestScheduleDto);

        // 비밀번호가 일치하는지 확인
        if (!(repository.findById(requestScheduleDto.getScheduleId()).getSchedulePassword().equals(requestScheduleDto.getSchedulePassword()))) {
            throw new IllegalArgumentException("비밀번호 미일치");
        }

        repository.update(requestScheduleDto);
    }

    public void callDelete(RequestScheduleDto requestScheduleDto) throws SQLException {

        if (Objects.isNull(requestScheduleDto.getScheduleId())) {
            throw new IllegalArgumentException("아이디 입력 필요");
        }

        Schedule findSchedule = repository.findById(requestScheduleDto.getScheduleId());
        log.debug("service findSchedule={}", findSchedule);
        log.debug("service requestSchedule={}", requestScheduleDto);

        if (!(findSchedule.getSchedulePassword().equals(requestScheduleDto.getSchedulePassword()))) {
            throw new IllegalArgumentException("비밀번호 미일치");
        }

        repository.delete(findSchedule);
    }
}
