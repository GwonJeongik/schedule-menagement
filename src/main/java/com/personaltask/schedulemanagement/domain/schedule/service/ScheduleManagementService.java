package com.personaltask.schedulemanagement.domain.schedule.service;

import com.personaltask.schedulemanagement.domain.schedule.model.Schedule;
import com.personaltask.schedulemanagement.domain.schedule.model.dto.ScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.repository.ManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ScheduleManagementService implements ManagementService<Schedule, ScheduleDto> {

    private final ManagementRepository<Schedule> repository;

    @Autowired
    public ScheduleManagementService(ManagementRepository<Schedule> repository) {
        this.repository = repository;
    }

    @Override
    public ScheduleDto callSave(Schedule schedule) throws SQLException {
        // 반환값이 필요가 없긴 한데...
        Schedule saveSchedule = repository.save(schedule);

        return new ScheduleDto(saveSchedule);
    }

    @Override
    public ScheduleDto callFindById(String id) throws SQLException {
        Schedule findSchedule = repository.findById(id);
        return null;
    }

    @Override
    public void callFindAll() {

    }

    @Override
    public ScheduleDto callUpdate(Schedule schedule) {
        return null;
    }

    @Override
    public void callDelete(Schedule schedule) {

    }
}
