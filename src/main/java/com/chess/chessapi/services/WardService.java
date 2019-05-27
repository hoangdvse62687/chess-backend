package com.chess.chessapi.services;

import com.chess.chessapi.entities.Ward;
import com.chess.chessapi.repositories.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardService {
    @Autowired
    private WardRepository wardRepository;

    public List<Ward> getAllByDistrictId(String districtId){
        return wardRepository.findAllByDistrictId(districtId);
    }
}
