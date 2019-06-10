package com.chess.chessapi.services;

import com.chess.chessapi.repositories.CategoryHasCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryHasCourseService {
    @Autowired
    private CategoryHasCourseRepository categoryHasCourseRepository;

    public void create(long categoryId,long courseId){
        this.categoryHasCourseRepository.create(categoryId,courseId);
    }
}
