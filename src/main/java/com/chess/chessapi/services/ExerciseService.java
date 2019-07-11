package com.chess.chessapi.services;

import com.chess.chessapi.entities.Course;
import com.chess.chessapi.entities.Exercise;
import com.chess.chessapi.repositories.ExerciseRepository;
import com.chess.chessapi.viewmodels.ExerciseCreateViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    public Optional<Exercise> getById(long exerciseId){
        return this.exerciseRepository.findById(exerciseId);
    }

    public long create(ExerciseCreateViewModel exerciseCreateViewModel){
        Exercise exercise = new Exercise();
        exercise.setQuestion(exerciseCreateViewModel.getQuestion());
        exercise.setAnswer(exerciseCreateViewModel.getAnswer());
        Course course = new Course();
        course.setCourseId(exerciseCreateViewModel.getCourseId());
        exercise.setCourse(course);
        Exercise savedExercise = this.exerciseRepository.save(exercise);
        return savedExercise.getExerciseId();
    }

    public void save(Exercise exercise){
        this.exerciseRepository.save(exercise);
    }

    public void deleteById(long exerciseId){
        this.exerciseRepository.deleteById(exerciseId);
    }

    public List<Exercise> getByCourseId(long courseId){
        return this.exerciseRepository.findByCourseId(courseId);
    }

    public long getCourseIdByExerciseId(long exerciseId){
        return this.exerciseRepository.findCourseIdByExerciseId(exerciseId);
    }

    public List<Long> getExerciseIdsByCourseId(long courseId){
        return this.exerciseRepository.findExerciseIdsByCourseId(courseId);
    }
}
