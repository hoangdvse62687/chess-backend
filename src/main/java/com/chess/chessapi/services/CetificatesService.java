package com.chess.chessapi.services;

import com.chess.chessapi.entities.Cetificates;
import com.chess.chessapi.repositories.CetificatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CetificatesService {
    @Autowired
    private CetificatesRepository cetificatesRepository;

    public Cetificates create(Cetificates cetificates){
        return cetificatesRepository.save(cetificates);
    }

}
