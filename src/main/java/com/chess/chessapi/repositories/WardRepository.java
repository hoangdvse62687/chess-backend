package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward,String> {
    List<Ward> findAllByDistrictId(String districtId);
}
