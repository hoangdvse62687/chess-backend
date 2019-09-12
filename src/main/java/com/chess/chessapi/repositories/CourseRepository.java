package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    @Query(value = "Update course c Set c.status_id = ?2,c.modified_date = ?3 where c.id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateStatus(long courseId, long statusId, Timestamp modifiedDate);

    @Query(value = "Update course c Set c.name = ?2,c.description = ?3,c.status_id = ?4,c.image = ?5,c.required_elo=?6" +
            ",c.modified_date = ?7" +
            " where c.id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateCourse(long courseId,String name,String description,
                      long statusId,String image,int requiredElo, Timestamp modifiedDate);

    @Query(value = "Select c.owner From course c where id = ?1",nativeQuery = true)
    Long findAuthorIdByCourseId(long courseId);

    @Query(value = "Select c.id,c.name,c.image From course c where c.id in ?1",nativeQuery = true)
    List<Object[]> findCourseDetailForNotificationByListCourseId(List<Long> listCourseId);

    @Query(value = "Select c.id,c.name,c.image From course c where c.id = ?1",nativeQuery = true)
    Object findCourseDetailForNotificationByCourseId(Long listCourseId);

    @Query(value = "Select id From course where required_elo = ?1 and status_id = ?2",nativeQuery = true)
    List<Long> findListCourseIdsByEloId(int eloId,long statusId);

    @Query(value = "Select count(id) From course where required_elo = ?1",nativeQuery = true)
    Integer countByEloId(int eloId);
}
