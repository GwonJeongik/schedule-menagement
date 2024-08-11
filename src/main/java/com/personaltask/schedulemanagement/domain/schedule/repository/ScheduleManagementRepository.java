package com.personaltask.schedulemanagement.domain.schedule.repository;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.dto.ResponseScheduleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
@Slf4j
public class ScheduleManagementRepository implements AutoCloseable, ManagementRepository<RequestScheduleDto, ResponseScheduleDto> {

    private final DataSource dataSource;
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet resultSet;

    @Autowired
    public ScheduleManagementRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        connection = null;
        pstmt = null;
        resultSet = null;
    }

    /**
     * DB에 schedule을 저장한다.
     */
    @Override
    public ResponseScheduleDto save(RequestScheduleDto requestScheduleDto) throws SQLException {

        try (ScheduleManagementRepository repository = new ScheduleManagementRepository(dataSource)) {
            String sql = "insert into Schedule" +
                    "(" +
                    "schedule_id," +
                    "schedule_password," +
                    "task," +
                    "admin_name," +
                    "registration_date," +
                    "modification_date" +
                    ")" +
                    "values(?, ?, ?, ?, ?, ?)";

            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement(sql);

            putValueInPstmt(requestScheduleDto);

            pstmt.execute(); // 쿼리문 실행

            // 저장한 schedule을 반환한다.
            return new ResponseScheduleDto(requestScheduleDto);

        } catch (SQLException e) {
            log.error("repository save error={}", e.getMessage());
            throw e;
        }
    }

    /**
     * preparedStatement에 쿼리문 value 값을 넣어준다.
     */
    private void putValueInPstmt(RequestScheduleDto requestScheduleDto) throws SQLException {
        // 스케쥴 아이디 생성
        String scheduleId = UUID.randomUUID().toString();
        // 스케쥴 생성 시간 -> 요구사항에 맞게 포멧팅 -> 나노초 부분 삭제
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        requestScheduleDto.setScheduleId(scheduleId);
        requestScheduleDto.setRegistrationDate(date);
        requestScheduleDto.setModificationDate(date);

        pstmt.setString(1, scheduleId); // 고유 id 자동 생성
        pstmt.setString(2, requestScheduleDto.getSchedulePassword());
        pstmt.setString(3, requestScheduleDto.getTask());
        pstmt.setString(4, requestScheduleDto.getAdminName());
        pstmt.setString(5, date); // 날짜 + 시간
        pstmt.setString(6, date); // 날짜 + 시간
    }

    /**
     * 아이디로 DB에 저장된 데이터를 찾을 수 있다.
     */
    @Override
    public ResponseScheduleDto findById(String scheduleId) throws SQLException {

        String sql = "select * from schedule where schedule_id = ?"; // 실행할 쿼리문 -> 스케쥴 아이디와 일치하는 놈을 찾아온다.

        /**
         * try with resource -> 사용한 자원을 반납한다.
         * close 메서드를 호출
         */
        try (ScheduleManagementRepository repository = new ScheduleManagementRepository(dataSource)) {
            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, scheduleId);
            pstmt.execute(); // 실행

            resultSet = pstmt.executeQuery(); // 실행 결과물 ResultSet으로 받음.

            if (!resultSet.next()) { // resultSet에 커서가 없으면 예외 터짐.
                throw new NoSuchElementException("등록된 스케쥴이 없습니다. 스케쥴 ID: " + scheduleId);
            }

            // 스케쥴 아이디와 일치하는 데이터를 Schedule 객체에 감싸서(담아서) 반환한다.
            return getSchedule();

        } catch (NoSuchElementException e) {
            System.out.println("[예외 발생]: " + e.getMessage());
            throw e;
        }
    }

    /**
     * resultSet(쿼리 실행 결과 - 조회)의 값을 새로운 Schedule 객체에 담아서 반환
     */
    private ResponseScheduleDto getSchedule() throws SQLException {
        ResponseScheduleDto responseScheduleDto = new ResponseScheduleDto();
        responseScheduleDto.setScheduleId(resultSet.getString("schedule_id"));
        responseScheduleDto.setTask(resultSet.getString("task"));
        responseScheduleDto.setAdminName(resultSet.getString("admin_name"));
        responseScheduleDto.setRegistrationDate(resultSet.getString("registration_date"));
        responseScheduleDto.setModificationDate(resultSet.getString("modification_date"));
        return responseScheduleDto;
    }

    public void findAll() {
    }

    public ResponseScheduleDto modify(RequestScheduleDto requestScheduleDto) {
        return null;
    }

    public void delete(RequestScheduleDto requestScheduleDto) {
        String sql = "delete from schedule where schedule_id = ?";
    }

    @Override
    public void close() {
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeConnection(connection);
    }
}
