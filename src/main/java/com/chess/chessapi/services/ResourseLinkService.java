package com.chess.chessapi.services;

import com.chess.chessapi.repositories.ResourseLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourseLinkService {
    @Autowired
    private ResourseLinkRepository resourseLinkRepository;

    public void deleteAllByUILesson(long uiLesson){
        this.resourseLinkRepository.deleteByUILesson(uiLesson);
    }
}
