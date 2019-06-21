package com.chess.chessapi.services;

import com.chess.chessapi.entities.Step;
import com.chess.chessapi.repositories.StepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepService {
    @Autowired
    private StepRepository stepRepository;

    public void create(Step step,long iLessonId){
        this.stepRepository.create(step.getContent(),step.getStepCode()
                ,step.getRightResponse(),step.getWrongResponse(),iLessonId,step.getOrderStep());
    }
    public void update(Step step){
        this.stepRepository.update(step.getContent(),step.getStepCode(),step.getRightResponse(),step.getWrongResponse()
        ,step.getOrderStep(),step.getStepId());
    }
    public List<Step> getAllByILessonId(long iLessonId){
        return this.stepRepository.findAllByILessonId(iLessonId);
    }

    public void deleteAllByILessonId(long iLessonId){
        this.stepRepository.deleteAllByILessonId(iLessonId);
    }

    public void updateSteps(List<Step> oldSteps,List<Step> newSteps,long iLessonId){
        int counter = 0;
        if(oldSteps == null || oldSteps.isEmpty()){
            for (Step newStep:
                 newSteps) {
                newStep.setOrderStep(counter);
                this.create(newStep,iLessonId);
                counter++;
            }
        }else if(newSteps != null && !newSteps.isEmpty()){
            for (Step newStep:
                    newSteps) {
                boolean isExist = false;
                for (Step oldStep:
                     oldSteps) {
                    if(oldStep.getStepId() == newStep.getStepId()){
                        isExist = true;
                        oldSteps.remove(oldStep);
                        break;
                    }
                }
                newStep.setOrderStep(counter);
                if(!isExist){
                    this.create(newStep,iLessonId);
                }else{
                    //update
                    this.update(newStep);
                }

                counter++;
            }

            for (Step oldStep:
                    oldSteps) {
                this.stepRepository.delete(oldStep);
            }
        }else{
            for (Step oldStep:
                    oldSteps) {
                this.stepRepository.delete(oldStep);
            }
        }
    }
}
