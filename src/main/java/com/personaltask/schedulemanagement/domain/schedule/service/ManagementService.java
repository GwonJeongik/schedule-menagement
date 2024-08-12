package com.personaltask.schedulemanagement.domain.schedule.service;

import java.sql.SQLException;
import java.util.List;

public interface ManagementService<T,D>  {

    public D callSave(T t) throws SQLException;

    public D callFindById(T t) throws SQLException;

    public List<D> callFindAll(T t) throws SQLException;

    public D callUpdate(T t) throws SQLException;

    public void callDelete(T t);
}
