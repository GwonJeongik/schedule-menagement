package com.personaltask.schedulemanagement.domain.schedule.repository;

import com.personaltask.schedulemanagement.domain.schedule.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.UUID;

@org.springframework.stereotype.Repository
public class ScheduleRepository implements AutoCloseable, Repository {

    private final DataSource dataSource;
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet resultSet;

    @Autowired
    public ScheduleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        connection = null;
        pstmt = null;
        resultSet = null;
    }

    @Override
    public Schedule findById(String scheduleId) throws SQLException {

        String sql = "select * from schedule where schedule_id = ?"; // 실행할 쿼리문 -> 스케쥴 아이디와 일치하는 놈을 찾아온다.

        /**
         * try with resource -> 사용한 자원을 반납한다.
         * close 메서드를 호출
         */
        try (ScheduleRepository repository = new ScheduleRepository(dataSource)) {
            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, scheduleId);
            pstmt.execute(); // 실행

            resultSet = pstmt.executeQuery(); // 실행 결과물 ResultSet으로 받음.

            if (!resultSet.next()) { // resultSet에 커서가 없으면 예외 터짐.
                throw new NoSuchElementException("등록된 스케쥴이 없습니다. 스케쥴 ID: " + scheduleId);
            }

            return getSchedule();

        } catch (NoSuchElementException e) {
            System.out.println("[예외 발생]: " + e.getMessage());
            throw e;
        }
    }

    private Schedule getSchedule() throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(resultSet.getString("schedule_id"));
        schedule.setSchedulePassword(resultSet.getString("schedule_password"));
        schedule.setTask(resultSet.getString("task"));
        schedule.setAdminName(resultSet.getString("admin_name"));
        schedule.setRegistrationDate(resultSet.getString("registration_date"));
        schedule.setModificationDate(resultSet.getString("modification_date"));
        return schedule;
    }

    public void findAll() {
    }

    @Override
    public Schedule save(Schedule schedule) throws SQLException {

        try (ScheduleRepository repository = new ScheduleRepository(dataSource)) {
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

            // 스케쥴 아이디 생성
            String scheduleId = UUID.randomUUID().toString();
            // 스케쥴 생성 시간
            String date = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            schedule.setScheduleId(scheduleId);
            schedule.setRegistrationDate(date);
            schedule.setModificationDate(date);

            pstmt.setString(1, schedule.getScheduleId()); // 고유 id 자동 생성
            pstmt.setString(2, schedule.getSchedulePassword());
            pstmt.setString(3, schedule.getTask());
            pstmt.setString(4, schedule.getAdminName());
            pstmt.setString(5, schedule.getRegistrationDate()); // 날짜 + 시간
            pstmt.setString(6, schedule.getModificationDate()); // 날짜 + 시간

            int count = pstmt.executeUpdate();
            System.out.println("등록된 수: " + count);

            return schedule;

        } catch (SQLException e) {
            System.out.println("DB Save Error");
            throw e;
        }
    }

    public void modify() {

    }

    public void delete(Schedule schedule) {
        String sql = "delete from schedule where schedule_id = ?";
    }

    @Override
    public void close() {
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeConnection(connection);
    }
}
