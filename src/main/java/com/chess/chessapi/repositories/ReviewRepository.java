package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query(value = "SELECT If(user_id = ?2,'true','false') From review Where id = ?1",nativeQuery = true)
    Boolean checkPermissionModifyReview(long reviewId,long userId);

    @Modifying
    @Transactional
    @Query(value = "Update review Set content = ?2,rating = ?3 Where id = ?1",nativeQuery = true)
    void update(long reviewId,String content,float rating);

    @Modifying
    @Transactional
    @Query(value = "Delete From review Where id = ?1",nativeQuery = true)
    void remove(long reviewId);
}
