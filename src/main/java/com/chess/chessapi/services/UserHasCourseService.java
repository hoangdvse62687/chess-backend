package com.chess.chessapi.services;

import com.chess.chessapi.repositories.UserHasCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHasCourseService {
    @Autowired
    private UserHasCourseRepository userHasCourseRepository;

    public void create(long userId,long courseId){
        this.userHasCourseRepository.create(userId,courseId);
    }
}
