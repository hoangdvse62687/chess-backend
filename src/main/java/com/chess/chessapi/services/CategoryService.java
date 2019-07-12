package com.chess.chessapi.services;

import com.chess.chessapi.entities.Category;
import com.chess.chessapi.repositories.CategoryRepository;
import com.chess.chessapi.utils.ManualCastUtils;
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

    @PersistenceContext
    private EntityManager em;

    public List<Category> getAllCategory(){
        return this.categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(long categoryId){
        return this.categoryRepository.findById(categoryId);
    }

    public void getCategoryDetails(Category category){
        if(category != null){
            category.setCourseDetailViewModels(this.getCourseDetails(category.getCategoryId()));
        }
    }

    public List<CourseDetailViewModel> getCourseDetails(long categoryId){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCourseByCategoryId");
        query.setParameter("categoryId",categoryId);

        query.execute();
        return ManualCastUtils.castListObjectToCourseDetails(query.getResultList());
    }
}
