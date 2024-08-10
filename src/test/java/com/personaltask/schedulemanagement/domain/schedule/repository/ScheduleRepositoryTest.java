package com.personaltask.schedulemanagement.domain.schedule.repository;

import com.personaltask.schedulemanagement.domain.schedule.model.Schedule;
import jdk.jfr.Name;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.personaltask.schedulemanagement.domain.schedule.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;

class ScheduleRepositoryTest {

    DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

    Repository repository = new ScheduleRepository(dataSource);

    @AfterEach
    void afterEach() throws SQLException {
        String sql = "truncate table schedule";

        Connection connection = dataSource.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.execute();
    }

    @Test
    @Name("스케쥴 모두 조회")
    void findAll() throws SQLException {
    }


    @Test
    @Name("스케쥴 저장 && 단일 조회")
    void save() throws SQLException {
        //given
        Schedule schedule = new Schedule("1234", "밥 먹기", "권정익");

        //when
        Schedule savedSchedule = repository.save(schedule);

        //then
        Schedule findSchedule = repository.findById(savedSchedule.getScheduleId());
        assertThat(savedSchedule).isEqualTo(findSchedule);
    }

    @Test
    @Name("스케쥴 수정")
    void modify() {
    }

    @Test
    @Name("스케쥴 삭제")
    void delete() {
    }
}