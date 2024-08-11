package com.personaltask.schedulemanagement.domain.schedule.service;

import java.sql.SQLException;

public interface ManagementService<T,D>  {

    public D callSave(T t) throws SQLException;

    public D callFindById(T t) throws SQLException;

    public void callFindAll();

    public D callUpdate(T t);

    public void callDelete(T t);
}
