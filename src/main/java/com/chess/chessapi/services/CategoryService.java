package com.chess.chessapi.services;

import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.entities.Category;
import com.chess.chessapi.repositories.CategoryRepository;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.viewmodels.CategoryViewModel;
import com.chess.chessapi.viewmodels.CourseDetailViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseService courseService;

    @PersistenceContext
    private EntityManager em;

    //public method
    public List<Category> getAllCategory(){
        return this.categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(long categoryId){
        return this.categoryRepository.findById(categoryId);
    }

    public void getCategoryDetails(Category category){
        if(category != null){
            category.setCourseDetailViewModels(this.courseService.getCourseDetailsByCategoryId(category.getCategoryId()));
        }
    }

    public List<Long> getListCategoryIdsByCourseId(long courseId){
        //getting category by courseid
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCategoryByCourseid");
        query.setParameter("courseId",courseId);

        query.execute();
        //end getting category by courseid
        return ManualCastUtils.castListObjectToCategoryIdFromGetCategoryByCourseId(query.getResultList());
    }

    public void create(CategoryViewModel category){
        this.categoryRepository.create(category.getName());
    }

    public void removeCategory(Category category){

        this.categoryRepository.delete(category);
    }

    public void update(CategoryViewModel category){
        this.categoryRepository.update(category.getCategoryId(),category.getName());
    }
    //end public method
}
