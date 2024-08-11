package com.personaltask.schedulemanagement.domain.schedule.service;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.dto.ResponseScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.repository.ManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ScheduleManagementService implements ManagementService<RequestScheduleDto, ResponseScheduleDto> {

    private final ManagementRepository<RequestScheduleDto, ResponseScheduleDto> repository;

    @Autowired
    public ScheduleManagementService(ManagementRepository<RequestScheduleDto, ResponseScheduleDto> repository) {
        this.repository = repository;
    }

    @Override
    public ResponseScheduleDto callSave(RequestScheduleDto requestScheduleDto) throws SQLException {
        // requestScheduleDto에 대한 검증 필요?
        return repository.save(requestScheduleDto);
    }

    @Override
    public ResponseScheduleDto callFindById(RequestScheduleDto requestScheduleDto) throws SQLException {
        String scheduleId = requestScheduleDto.getScheduleId();
        return repository.findById(scheduleId);
    }

    @Override
    public void callFindAll() {

    }

    @Override
    public ResponseScheduleDto callUpdate(RequestScheduleDto requestScheduleDto) {
        return null;
    }

    @Override
    public void callDelete(RequestScheduleDto requestScheduleDto) {

    }
}
