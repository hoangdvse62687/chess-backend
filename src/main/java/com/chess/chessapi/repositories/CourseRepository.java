package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    @Query(value = "Update course c Set c.status_id = ?2 where c.id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateStatus(long courseId,long statusId);

    @Query(value = "Update course c Set c.name = ?2,c.description = ?3,c.point = ?4,c.status_id = ?5,c.image = ?6" +
            " where c.id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateCourse(long courseId,String name,String description,Float point,long statusId,String image);

    @Query(value = "Select c.owner From course c where id = ?1",nativeQuery = true)
    Long findAuthorIdByCourseId(long courseId);
}
