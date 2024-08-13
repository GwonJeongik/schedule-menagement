package com.personaltask.schedulemanagement.domain.schedule.service;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.dto.ResponseScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
// 제네릭은 좀 무겁다.
// 에러 처리 컨트롤러 vs 서비스
public class ScheduleService {

    private final ScheduleRepository repository;

    public ScheduleService(ScheduleRepository repository) {
        // requestScheduleDto에 대한 검증 필요? -> 일괄 확인
        this.repository = repository;
    }

    public ResponseScheduleDto callSave(RequestScheduleDto requestScheduleDto) throws SQLException {

        // **요청 값을 서비스에서 검증**
        // try catch -> 예외 래핑 -> 처음 발생했던 예외가 감싸져서 디버깅이 힘듦.
        return repository.save(requestScheduleDto);
    }

    public ResponseScheduleDto callFindById(RequestScheduleDto requestScheduleDto) throws SQLException {
        // requestScheduleDto에 대한 검증 필요?
        String scheduleId = requestScheduleDto.getScheduleId();
        return repository.findById(scheduleId);
    }

    public List<ResponseScheduleDto> callFindAll(RequestScheduleDto requestScheduleDto) throws SQLException {
        // requestScheduleDto에 대한 검증 필요?
        return repository.findAll(requestScheduleDto);
    }

    public ResponseScheduleDto callUpdate(RequestScheduleDto requestScheduleDto) throws SQLException {
        // requestScheduleDto에 대한 검증 필요?
        repository.update(requestScheduleDto);
        return repository.findById(requestScheduleDto.getScheduleId());
    }

    public void callDelete(RequestScheduleDto requestScheduleDto) {

    }
}
