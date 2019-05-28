package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Cetificates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CetificatesRepository extends JpaRepository<Cetificates,Long> {
    @Query(value = "Select * From cetificates c where c.fk_user = ?1",nativeQuery = true)
    List<Cetificates> findAllByUserId(long userId);
}
