package com.chess.chessapi.services;

import com.chess.chessapi.entities.Province;
import com.chess.chessapi.repositories.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;

    public List<Province> getAllProvince(){
        return provinceRepository.findAll();
    }
}
