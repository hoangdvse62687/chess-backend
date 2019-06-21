package com.chess.chessapi.services;

import com.chess.chessapi.entities.CategoryHasCourse;
import com.chess.chessapi.repositories.CategoryHasCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryHasCourseService {
    @Autowired
    private CategoryHasCourseRepository categoryHasCourseRepository;

    //public method
    public void create(long categoryId,long courseId){
        this.categoryHasCourseRepository.create(categoryId,courseId);
    }

    public List<CategoryHasCourse> getAllByCourseId(long courseId){
        return this.categoryHasCourseRepository.findAllByCourseId(courseId);
    }

    public void UpdateCategoryHasCourse(List<CategoryHasCourse> oldCategoryHasCourses,List<Long> newCategoryHasCourses,long courseId){
        if(oldCategoryHasCourses == null || oldCategoryHasCourses.isEmpty()){
            for (Long newCategoryHasCourse:
                 newCategoryHasCourses) {
                this.categoryHasCourseRepository.create(newCategoryHasCourse,courseId);
            }
        }else if(newCategoryHasCourses != null && !newCategoryHasCourses.isEmpty()){
            for (Long newCategoryHasCourse:
                    newCategoryHasCourses) {
                boolean isExist = false;

                for (CategoryHasCourse oldCategoryHasCourse:
                        oldCategoryHasCourses) {
                    if(newCategoryHasCourse == oldCategoryHasCourse.getCategory().getCategoryId()) {
                        isExist = true;
                        oldCategoryHasCourses.remove(oldCategoryHasCourse);
                        break;
                    }
                }

                if(!isExist){
                    this.create(newCategoryHasCourse,courseId);
                }
            }

            //delete old
            for (CategoryHasCourse oldCategoryHasCourse:
                    oldCategoryHasCourses) {
                this.categoryHasCourseRepository.delete(oldCategoryHasCourse);
            }
        }else{
            for (CategoryHasCourse oldCategoryHasCourse:
                oldCategoryHasCourses) {
                this.categoryHasCourseRepository.delete(oldCategoryHasCourse);
            }
        }
    }
    //end public method
}
