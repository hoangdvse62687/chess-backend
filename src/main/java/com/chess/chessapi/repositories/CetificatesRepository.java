package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Cetificates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CetificatesRepository extends JpaRepository<Cetificates,Long> {

}
