package com.personaltask.schedulemanagement.domain.schedule.repository;

import com.personaltask.schedulemanagement.domain.schedule.model.Schedule;

import java.sql.SQLException;

public interface Repository {

    public Schedule findById(String Id) throws SQLException;

    public void findAll();

    public Schedule save(Schedule schedule)throws SQLException;

    public void modify();

    public void delete(Schedule schedule);
}
