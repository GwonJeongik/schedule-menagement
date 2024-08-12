package com.personaltask.schedulemanagement.domain.schedule.repository;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.dto.ResponseScheduleDto;
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

class RequestResponseScheduleDtoManagementRepositoryTest {

    DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

    ManagementRepository<RequestScheduleDto, ResponseScheduleDto> managementRepository = new ScheduleManagementRepository(dataSource);

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
        //given
        //when
        //then
    }


    @Test
    @Name("스케쥴 저장 && 단일 조회")
    void save() throws SQLException {
        //given
        RequestScheduleDto requestScheduleDto
                = new RequestScheduleDto("1234", "화장실 가기", "권정익");

        //when
        ResponseScheduleDto saveResponseSchedule = managementRepository.save(requestScheduleDto);

        //then
        ResponseScheduleDto findRequestScheduleDto = managementRepository.findById(saveResponseSchedule.getScheduleId());
        assertThat(saveResponseSchedule).isEqualTo(findRequestScheduleDto);
    }

    @Test
    @Name("스케쥴 수정")
    void update() {
        //given
        //when
        //then
    }

    @Test
    @Name("스케쥴 삭제")
    void delete() {
        //given
        //when
        //then
    }
}