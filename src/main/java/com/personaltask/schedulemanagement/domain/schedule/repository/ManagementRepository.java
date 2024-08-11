package com.personaltask.schedulemanagement.domain.schedule.repository;

import java.sql.SQLException;

/**
 * AdminRepository에도 쓰고 싶은데...
 * 매개변수를 담당자쪽과 스케쥴쪽 모두 사용할 수 있게 하려면?
 * 근데 의존관계 주입을 각자 어떻게 받지? -> 빈 2개 이상일 때로 검색?
 * 그럼 서비스도 인터페이스를 이용해서 각각 구현? -> 각각 구현한 담당자, 스케쥴 서비스에 자신에게 맞는 repository 주입
 *
 * 제네릭을 이용하면 admin 객체 || schedule 객체를 받을 수 있다.
 */
public interface ManagementRepository<T, D> {

    public D save(T t)throws SQLException;

    public D findById(String id) throws SQLException;

    public void findAll();

    public D modify(T t);

    public void delete(T t);
}
