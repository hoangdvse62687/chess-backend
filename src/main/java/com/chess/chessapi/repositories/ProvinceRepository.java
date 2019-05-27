package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository  extends JpaRepository<Province,String> {

}
