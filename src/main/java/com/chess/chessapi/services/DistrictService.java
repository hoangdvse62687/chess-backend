package com.chess.chessapi.services;

import com.chess.chessapi.entities.District;
import com.chess.chessapi.repositories.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    public List<District> getAllByProvinceId(String provinceId){
        return districtRepository.findAllByProvinceId(provinceId);
    }
}
