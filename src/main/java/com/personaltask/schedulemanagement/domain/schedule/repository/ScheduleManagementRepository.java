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
import java.util.*;

@Repository
@Slf4j
public class ScheduleManagementRepository
        implements AutoCloseable, ManagementRepository<RequestScheduleDto, ResponseScheduleDto> {

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
     * 레벨 1
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

            pstmt.executeUpdate(); // 쿼리문 실행

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
     * 레벨 2
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
            return getResponseSchedule();
        }
    }

    /**
     * resultSet(쿼리 실행 결과 - 조회)의 값을 새로운 Schedule 객체에 담아서 반환
     */
    private ResponseScheduleDto getResponseSchedule() throws SQLException {
        ResponseScheduleDto responseScheduleDto = new ResponseScheduleDto();
        responseScheduleDto.setScheduleId(resultSet.getString("schedule_id"));
        responseScheduleDto.setTask(resultSet.getString("task"));
        responseScheduleDto.setAdminName(resultSet.getString("admin_name"));
        responseScheduleDto.setRegistrationDate(resultSet.getString("registration_date"));
        responseScheduleDto.setModificationDate(resultSet.getString("modification_date"));
        return responseScheduleDto;
    }

    /**
     * 날짜와 담당자가 일치하는 일정 모두 찾기
     * 날짜만 일치, 담당자만 일치는 아직 구현 x -> 문제가 이해하기 어렵게 표시되어있음.
     */
    public List<ResponseScheduleDto> findAll(RequestScheduleDto requestScheduleDto) throws SQLException {
        String sql= "select * from schedule " +
                "where (date(modification_date) = ? or ? is null) and (admin_name = ? or ? is null) " +
                "order by modification_date desc";

        connection = dataSource.getConnection();
        pstmt = connection.prepareStatement(sql);

        pstmt.setString(1, requestScheduleDto.getModificationDate());
        pstmt.setString(2, requestScheduleDto.getModificationDate());
        pstmt.setString(3, requestScheduleDto.getAdminName());
        pstmt.setString(4, requestScheduleDto.getAdminName());

        pstmt.execute();

        resultSet = pstmt.getResultSet();

        if (Objects.isNull(resultSet)) {
            throw new NoSuchElementException(
                    "등록된 스케쥴이 없습니다. 요청 수정일: " + requestScheduleDto.getModificationDate()
            );
        }

        ArrayList<ResponseScheduleDto> responseScheduleList = new ArrayList<>();

        while (resultSet.next()){
            responseScheduleList.add(getResponseSchedule());
        };

        return responseScheduleList;
    }

    /**
     * 등록된 스케쥴의 할 일과 담당자명을 변경
     * 둘 중 하나만 들어와도 들어온 값만 변경
     *
     */
    public void update(RequestScheduleDto requestScheduleDto) throws SQLException {
        String sql = "update schedule " +
                "set modification_date = ?, " + // 1
                "task = case when ? is not null then ? else task end, " + // 2
                "admin_name = case when ? is not null then ? else admin_name end " + //2
                "where schedule_id = ? and schedule_password = ?"; // 2

        connection = dataSource.getConnection();
        pstmt = connection.prepareStatement(sql);

        String newModificationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        pstmt.setString(1, newModificationDate);
        pstmt.setString(2, requestScheduleDto.getTask());
        pstmt.setString(3, requestScheduleDto.getTask());
        pstmt.setString(4, requestScheduleDto.getAdminName());
        pstmt.setString(5, requestScheduleDto.getAdminName());
        pstmt.setString(6, requestScheduleDto.getScheduleId());
        pstmt.setString(7, requestScheduleDto.getSchedulePassword());

        pstmt.executeUpdate();
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
