package com.chess.chessapi.services;

import com.chess.chessapi.constants.EntitiesFieldName;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.repositories.LessonRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private StepService stepService;

    @Autowired
    private InteractiveLessonService interactiveLessonService;

    @Autowired
    private CourseHasLessonService courseHasLessonService;

    @Autowired
    private UserService userService;

    @Autowired
    private UninteractiveLessonService uninteractiveLessonService;

    @Autowired
    private LearningLogService learningLogService;

    @PersistenceContext
    private EntityManager em;

    //PUBLIC METHOD DEFINED
    public Optional<Lesson> getById(long id){
        return this.lessonRepository.findById(id);
    }

    public List<LessonViewModel> getLessonByCourseId(long courseId){
        //getting courses by userId
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getLessonByCourseId");
        query.setParameter("courseId",courseId);

        query.execute();
        //end getting courses by userid

        return ManualCastUtils.castListObjectToLessonViewModel(query.getResultList());
    }

    public long getAuthorLessonByLessonId(long lessonId){
        return this.lessonRepository.findLessonAuthorByLessonId(lessonId);
    }

    public long createInteractiveLesson(InteractiveLessonCreateViewModel lessonViewModel, long userId){
        //Create Lesson
        Lesson savedLesson = this.createLesson(lessonViewModel.getName(),userId);
        //create interactive lesson
        InteractiveLesson interactiveLesson = lessonViewModel.getInteractiveLesson();
        interactiveLesson.setLesson(savedLesson);
        InteractiveLesson savedInteractiveLesson = this.interactiveLessonService.create(interactiveLesson);
        //create steps in interactive lesson
        int counter = 0;
        for (Step step:
                lessonViewModel.getInteractiveLesson().getSteps()) {
            step.setOrderStep(counter);
            this.stepService.create(step,savedInteractiveLesson.getInteractiveLessonId());
            counter++;
        }
        //create mapping course has lesson in case has course id
        if(lessonViewModel.getCourseId() != 0){
            //calculate next lesson ordered
            int lessonOrder = this.courseHasLessonService.getLastestLessonOrder(lessonViewModel.getCourseId());
            lessonOrder++;
            this.courseHasLessonService.create(savedLesson.getLessonId()
                    ,lessonViewModel.getCourseId(),lessonOrder);
        }
        return savedLesson.getLessonId();
    }

    public long createUninteractiveLesson(UninteractiveLessonCreateViewModel uninteractiveLessonCreateViewModel,long userId){
        //Create Lesson
        Lesson savedLesson = this.createLesson(uninteractiveLessonCreateViewModel.getName(),userId);
        //create uninteractive lesson
        UninteractiveLesson uninteractiveLesson = new UninteractiveLesson();
        uninteractiveLesson.setContent(uninteractiveLessonCreateViewModel.getContent());
        uninteractiveLesson.setLesson(savedLesson);
        this.uninteractiveLessonService.create(uninteractiveLesson);

        //create mapping course has lesson in case has course id
        if(uninteractiveLessonCreateViewModel.getCourseId() != 0){
            //calculate next lesson ordered
            int lessonOrder = this.courseHasLessonService.getLastestLessonOrder(uninteractiveLessonCreateViewModel.getCourseId());
            lessonOrder++;
            this.courseHasLessonService.create(savedLesson.getLessonId()
                    ,uninteractiveLessonCreateViewModel.getCourseId(),lessonOrder);
        }
        return savedLesson.getLessonId();
    }

    public void updateInteractiveLesson(InteractiveLessonUpdateViewModel lessonViewModel){
        //update lesson
        this.updateLesson(lessonViewModel.getLessonId(),lessonViewModel.getName());

        //update interactive lesson info
        this.interactiveLessonService.update(lessonViewModel.getInteractiveLesson().getInteractiveLessonId()
                ,lessonViewModel.getInteractiveLesson().getInitCode());

        //update steps
        List<Step> oldSteps = this.stepService.getAllByILessonId(lessonViewModel.getInteractiveLesson().getInteractiveLessonId());
        this.stepService.updateSteps(oldSteps,lessonViewModel.getInteractiveLesson().getSteps(),lessonViewModel.getInteractiveLesson().getInteractiveLessonId());
    }

    public void updateUninteractiveLesson(UninteractiveLessonUpdateViewModel uninteractiveLessonUpdateViewModel){
        //update lesson
        this.updateLesson(uninteractiveLessonUpdateViewModel.getLessonId(),uninteractiveLessonUpdateViewModel.getName());

        //update uninteractive lesson info
        this.uninteractiveLessonService.update(uninteractiveLessonUpdateViewModel.getUninteractiveLesson().getUninteractiveLessonId(),
                uninteractiveLessonUpdateViewModel.getUninteractiveLesson().getContent());

    }

    public void updateLesson(long lessonId,String name){
        this.lessonRepository.update(lessonId,name);
    }

    public PagedList<LessonViewModel> getAllByOwner(int page, int pageSize, String name, long userId){
        PageRequest pageable =  null;
        pageable = PageRequest.of(page - 1,pageSize, Sort.by(EntitiesFieldName.LESSON_CREATED_DATE).descending());
        Page<Object> rawData = this.lessonRepository.findAllByOwner(pageable,name,userId);
        return this.fillDataToPagination(rawData);
    }

    public boolean checkPermissionModifyLesson(long lessonId){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        if(userPrincipal != null){
            long ownerLessonId = this.lessonRepository.findLessonAuthorByLessonId(lessonId);
            if(ownerLessonId == userPrincipal.getId()){
                return true;
            }
        }
        return false;
    }

    public void removeLesson(Lesson lesson){
        //remove all learning log
        this.learningLogService.deleteAllByLessonId(lesson.getLessonId());
        if(lesson.getInteractiveLesson() != null){
            //remove all steps
            this.stepService.deleteAllByILessonId(lesson.getInteractiveLesson().getInteractiveLessonId());
        }

        //delete mapping with course
        this.courseHasLessonService.deleteAllByLessonId(lesson.getLessonId());
        //delete lesson
        this.lessonRepository.delete(lesson);
    }

    public boolean isExist(long lessonId){
        return this.lessonRepository.existsById(lessonId);
    }
    //END PUBLIC METHOD DEFINED

    //PRIVATE METHOD DEFINED
    private PagedList<LessonViewModel> fillDataToPagination(Page<Object> rawData){
        final List<LessonViewModel> content = ManualCastUtils.castPageObjectToLessonViewModel(rawData);
        final int totalPages = rawData.getTotalPages();
        final long totalElements = rawData.getTotalElements();
        return new PagedList<LessonViewModel>(totalPages,totalElements,content);
    }

    private Lesson createLesson(String name,long userId){
        Lesson lesson = new Lesson();
        lesson.setName(name);
        lesson.setCreatedDate(TimeUtils.getCurrentTime());
        User user = new User();
        user.setUserId(userId);
        lesson.setUser(user);
        return this.lessonRepository.save(lesson);
    }
    //END PRIVATE METHOD DEFINED
}
