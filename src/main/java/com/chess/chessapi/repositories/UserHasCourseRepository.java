package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.UserHasCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserHasCourseRepository extends JpaRepository<UserHasCourse,Long> {
    @Modifying
    @Transactional
    @Query(value = "Insert into user_has_course (user_id,course_id,enrolled_date,status_id) " +
            "VALUES (?1,?2,?3,?4)"
            ,nativeQuery = true)
    void create(long userId, long courseId, Timestamp enrolledDate,long statusId);

    @Modifying
    @Transactional
    @Query(value = "Update user_has_course Set user_id = ?1,course_id = ?2,status_id = ?3 where  id = ?3"
            ,nativeQuery = true)
    void update(long userId,long courseId,long userHasCourseId,long statusId);

    @Query(value = "Select * From user_has_course where course_id = ?1 and status_id = ?2"
            ,nativeQuery = true)
    List<UserHasCourse> findAllByCourseIdAndStatusId(long courseId,long statusId);


}
