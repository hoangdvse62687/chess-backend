package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District,String> {
    List<District> findAllByProvinceId(String provinceId);
}
