package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.UserHasCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserHasCourseRepository extends JpaRepository<UserHasCourse,Long> {
    @Modifying
    @Transactional
    @Query(value = "Insert into user_has_course (user_id,course_id) " +
            "VALUES (?1,?2)"
            ,nativeQuery = true)
    void create(long userId,long courseId);
}
