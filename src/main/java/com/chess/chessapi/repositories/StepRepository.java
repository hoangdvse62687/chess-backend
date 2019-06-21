package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface StepRepository extends JpaRepository<Step,Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT  into step (content,step_code,right_response,wrong_response,ilesson_id,order_step) " +
            "VALUES (?1,?2,?3,?4,?5,?6)"
            ,nativeQuery = true)
    void create(String content,String stepCode
            ,String rightResponse,String wrongResponse,long ilessonId,int orderStep);

    @Modifying
    @Transactional
    @Query(value = "Update step Set content = ?1,step_code = ?2,right_response=?3,wrong_response=?4" +
            ",order_step=?5 where id = ?6",nativeQuery = true)
    void update(String content,String stepCode
            ,String rightResponse,String wrongResponse,int orderStep,long id);

    @Modifying
    @Transactional
    @Query(value = "Delete From step where ilesson_id = ?1",nativeQuery = true)
    void deleteAllByILessonId(long iLessonId);

    @Query(value = "Select * From step where ilesson_id = ?1",nativeQuery = true)
    List<Step> findAllByILessonId(long ILessonId);
}
