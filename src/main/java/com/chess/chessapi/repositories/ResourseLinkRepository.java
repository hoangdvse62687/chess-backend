package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.ResourseLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ResourseLinkRepository extends JpaRepository<ResourseLink,Long> {
    @Modifying
    @Transactional
    @Query(value = "Delete From resourse_link where uilesson_id ?1",nativeQuery = true)
    void deleteByUILesson(long uiLesson);
}
