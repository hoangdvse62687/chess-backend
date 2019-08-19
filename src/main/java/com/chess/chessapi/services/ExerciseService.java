package com.chess.chessapi.services;

import com.chess.chessapi.entities.Exercise;
import com.chess.chessapi.repositories.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    //PUBLIC METHOD DEFINED
    public Optional<Exercise> getById(long exerciseId){
        return this.exerciseRepository.findById(exerciseId);
    }

    public Exercise create(Exercise exercise){
        return this.save(exercise);
    }

    public void update(long lessonId,String question,String answer){
        this.exerciseRepository.update(lessonId,question,answer);
    }

    public Exercise save(Exercise exercise){
        return this.exerciseRepository.save(exercise);
    }

    //PUBLIC METHOD DEFINED

    //PRIVATE METHOD DEFINED

    //END PRIVATE METHOD DEFINED
}
