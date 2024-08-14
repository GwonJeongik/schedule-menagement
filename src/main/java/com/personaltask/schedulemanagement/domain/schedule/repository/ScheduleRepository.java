package com.personaltask.schedulemanagement.domain.schedule.repository;

import com.personaltask.schedulemanagement.domain.schedule.dto.RequestScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.dto.ResponseScheduleDto;
import com.personaltask.schedulemanagement.domain.schedule.entity.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@Slf4j
public class ScheduleRepository {

    private final DataSource dataSource;
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet resultSet;

    public ScheduleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        connection = null;
        pstmt = null;
        resultSet = null;
    }

    /**
     * 레벨 1
     * DB에 schedule을 저장한다.
     */
    public Schedule save(Schedule schedule) throws SQLException {

        try {
            // 신박한 거~ text block(텍스트 블럭)
            String sql = """
                                insert into Schedule (
                                  schedule_id,
                                  schedule_password,
                                  task,
                                  admin_name,
                                  registration_date,
                                  modification_date) values(?, ?, ?, ?, ?, ?)
                    """;

            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement(sql);

            putValueInPstmt(schedule);

            pstmt.executeUpdate(); // 쿼리문 실행

            // 저장한 schedule entity를 반환한다.
            return schedule;

        } finally {
            // 사용한 자원 정리 -> 역순으로 닫아준다.
            closeResource();
        }
    }

    /**
     * preparedStatement에 쿼리문 value 값을 넣어준다.
     */
    private void putValueInPstmt(Schedule schedule) throws SQLException {
        // 스케쥴 아이디 생성 -> uuid 희박한 확률로 겹칠 수 있다. -> 검증 필요 -> autoincrement
        String scheduleId = UUID.randomUUID().toString();

        // 스케쥴 생성 시간 -> 요구사항에 맞게 포멧팅 -> 나노초 부분 삭제
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        schedule.setScheduleId(scheduleId);
        schedule.setRegistrationDate(date);
        schedule.setModificationDate(date);

        pstmt.setString(1, schedule.getScheduleId()); // 고유 id 자동 생성
        pstmt.setString(2, schedule.getSchedulePassword());
        pstmt.setString(3, schedule.getTask());
        pstmt.setString(4, schedule.getAdminName());
        pstmt.setString(5, schedule.getRegistrationDate()); // 날짜 + 시간
        pstmt.setString(6, schedule.getModificationDate()); // 날짜 + 시간
    }

    /**
     * 레벨 2
     * 아이디로 DB에 저장된 데이터를 찾을 수 있다.
     */
    public Schedule findById(String scheduleId) throws SQLException {

        String sql = "select * from schedule where schedule_id = ?"; // 실행할 쿼리문 -> 스케쥴 아이디와 일치하는 놈을 찾아온다.

        try {
            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, scheduleId);

            pstmt.execute(); // 실행

            resultSet = pstmt.getResultSet(); // 실행 결과물 ResultSet으로 받음.

            // resultSet에 커서를 이동해서 조회된 데이터가 있는지 확인.
            if (!resultSet.next()) {
                throw new NoSuchElementException("등록된 스케쥴이 없습니다. 스케쥴 ID: " + scheduleId);
            }

            // 스케쥴 아이디와 일치하는 데이터를 Schedule 객체에 감싸서(담아서) 반환한다.
            return getResponseSchedule();

        } finally {
            // 사용한 자원 정리 -> 역순으로 닫아준다.
            closeResource();
        }
    }

    /**
     * resultSet(쿼리 실행 결과 - 조회)의 값을 새로운 Schedule 객체에 담아서 반환
     */
    private Schedule getResponseSchedule() throws SQLException {
        Schedule findSchedule = new Schedule();
        findSchedule.setScheduleId(resultSet.getString("schedule_id"));
        findSchedule.setSchedulePassword(resultSet.getString("schedule_password"));
        findSchedule.setTask(resultSet.getString("task"));
        findSchedule.setAdminName(resultSet.getString("admin_name"));
        findSchedule.setRegistrationDate(resultSet.getString("registration_date"));
        findSchedule.setModificationDate(resultSet.getString("modification_date"));
        return findSchedule;
    }

    /**
     * 레벨 3
     * 조건을 바탕으로 일정 목록 모두 조회
     */
    public List<Schedule> findAll(Schedule schedule) throws SQLException {

        String sql = """
                select * from schedule
                where (date(modification_date) = ? or ? is null) and (admin_name = ? or ? is null)
                order by modification_date desc;
                """;

        connection = dataSource.getConnection();
        pstmt = connection.prepareStatement(sql);

        pstmt.setString(1, schedule.getModificationDate());
        pstmt.setString(2, schedule.getModificationDate());
        pstmt.setString(3, schedule.getAdminName());
        pstmt.setString(4, schedule.getAdminName());

        pstmt.execute();

        resultSet = pstmt.getResultSet();

        if (!resultSet.next()) {
            throw new NoSuchElementException("등록된 스케쥴이 없습니다. 요청 수정일: " + schedule.getModificationDate());
        }

        ArrayList<Schedule> scheduleList = new ArrayList<>();
        do {
            Schedule addList = getResponseSchedule();
            scheduleList.add(addList);
        } while (resultSet.next());

        return scheduleList;
    }

    /**
     * 레벨 4
     * 등록된 스케쥴의 할 일과 담당자명을 변경
     * 둘 중 하나만 들어와도 들어온 값만 변경
     * 요청으로 온 비밀번호와 일치해야함.
     * 일치하지 않을 시, 적절한 오류 코드 & 메시지 반환
     */
    public void update(RequestScheduleDto requestScheduleDto) throws SQLException {

        String sql = """
                update schedule set
                 modification_date = ?,
                 task = case when ? is not null and ? <> '' then ? else task end,
                 admin_name = case when ? is not null and ? <> '' then ? else admin_name end
                where schedule_id = ?;
                """;

        connection = dataSource.getConnection();
        pstmt = connection.prepareStatement(sql);
        log.info("repository schedule={}", requestScheduleDto);

        // 수정일 현재로 변경
        String newModificationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        pstmt.setString(1, newModificationDate);
        pstmt.setString(2, requestScheduleDto.getTask());
        pstmt.setString(3, requestScheduleDto.getTask());
        pstmt.setString(4, requestScheduleDto.getTask());
        pstmt.setString(5, requestScheduleDto.getAdminName());
        pstmt.setString(6, requestScheduleDto.getAdminName());
        pstmt.setString(7, requestScheduleDto.getAdminName());
        pstmt.setString(8, requestScheduleDto.getScheduleId());

        // 변경 쿼리 실행
        pstmt.execute();
    }

    public void delete(Schedule schedule) throws SQLException {
        String sql = "delete from schedule where schedule_id = ?";

        connection = dataSource.getConnection();
        pstmt = connection.prepareStatement(sql);

        pstmt.setString(1, schedule.getScheduleId());

        pstmt.execute();
    }

    public void closeResource() {
        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeConnection(connection);
    }
}
