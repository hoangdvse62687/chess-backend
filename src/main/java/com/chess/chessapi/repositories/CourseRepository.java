package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    @Query(value = "Update course c Set c.status_id = ?2 where c.id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateStatus(long courseId,long statusId);
}
