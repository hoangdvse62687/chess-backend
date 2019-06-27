package com.chess.chessapi.services;

import com.chess.chessapi.constants.*;
import com.chess.chessapi.entities.*;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.repositories.CourseRepository;
import com.chess.chessapi.repositories.NotificationRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.CourseCreateViewModel;
import com.chess.chessapi.viewmodels.CourseDetailViewModel;
import com.chess.chessapi.viewmodels.CoursePaginationViewModel;
import com.chess.chessapi.viewmodels.UserDetailViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Optional;


@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserHasCourseService userHasCourseService;

    @Autowired
    private CategoryHasCourseService categoryHasCourseService;


    //Public method
    public Course create(CourseCreateViewModel courseCreateViewModel, long userId){
        //default setting when created course is inactive
        courseCreateViewModel.setStatusId(Status.COURSE_STATUS_DRAFTED);
        Course course = ManualCastUtils.castCourseCreateViewModelToCourse(courseCreateViewModel);
        course.setCreatedDate(TimeUtils.getCurrentTime());
        //manual mapping author
        User user = new User();
        user.setUserId(userId);
        course.setUser(user);
        Course savedCourse = courseRepository.save(course);

        return savedCourse;
    }

    public PagedList<CoursePaginationViewModel> getCoursePaginationByStatusId(String courseName,int pageIndex, int pageSize, String statusId,long userId)
            throws NumberFormatException{
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("getCoursePaginations");
        storedProcedureQuery.setParameter("courseName",courseName);
        storedProcedureQuery.setParameter("pageIndex",(pageIndex - 1) * pageSize);
        storedProcedureQuery.setParameter("pageSize",pageSize);
        storedProcedureQuery.setParameter("statusId",statusId);
        storedProcedureQuery.setParameter("userId",userId);
        storedProcedureQuery.setParameter("totalElements",Long.parseLong("0"));


        storedProcedureQuery.execute();

        List<Object[]> rawData = storedProcedureQuery.getResultList();
        final long totalElements = Long.parseLong(storedProcedureQuery.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
    }

    public PagedList<CoursePaginationViewModel> getCoursePaginationsByCategoryId(int pageIndex,int pageSize,long categoryId,long userId){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCourseByCategoryId");
        query.setParameter("pageSize",pageSize);
        query.setParameter("pageIndex",(pageIndex - 1) * pageSize);
        query.setParameter("userId",userId);
        query.setParameter("categoryId",categoryId);
        query.setParameter("totalElements",Long.parseLong("0"));

        query.execute();
        List<Object[]> rawData = query.getResultList();
        final long totalElements = Long.parseLong(query.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
    }

    public void updateStatus(long courseId,long statusId){
        this.courseRepository.updateStatus(courseId,statusId);
    }

    public Optional<Course> getCourseById(long id){
        return this.courseRepository.findById(id);
    }

    public List<CourseDetailViewModel> getCourseDetailsByUserId(long userId){
        //getting courses by userId
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCourseByUserId");
        query.setParameter("userId",userId);

        query.execute();
        //end getting courses by userid

        return ManualCastUtils.castListObjectToCourseDetails(query.getResultList());
    }

    public List<CourseDetailViewModel> getCourseDetailsByLessonId(long lessonId){
        //getting courses by userId
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCoursesByLessonId");
        query.setParameter("lessonId",lessonId);

        query.execute();
        //end getting courses by userid

        return ManualCastUtils.castListObjectToCourseDetails(query.getResultList());
    }

    public boolean checkPermissionViewCourseDetail(long courseId,UserPrincipal userPrincipal){
        if(userPrincipal == null){
            return false;
        }

        //allow admin permission view course
        if(Long.parseLong(userPrincipal.getRole()) == AppRole.ROLE_ADMIN){
            return true;
        }

        return this.callStoreProcedureCheckPermission(courseId,userPrincipal.getId());
    }

    public boolean checkPermissionModifyCourse(long courseId){
        //check current user has this course
        //only method authorized used this method => not check null
        UserPrincipal userPrincipal = this.userService.getCurrentUser();

        //it's only used instructor authentication => check for modifying course
        return this.callStoreProcedureCheckPermission(courseId,userPrincipal.getId());
    }

    public void updateCourse(Course course){
         this.courseRepository.updateCourse(course.getCourseId(),course.getName(),course.getDescription(),
                 course.getPoint(),course.getStatusId(),course.getImage());
         //only get old has status in-process
        List<UserHasCourse> oldUserHasCourses = this.userHasCourseService
                .getAllByCourseIdAndStatusId(course.getCourseId(),Status.USER_HAS_COURSE_STATUS_IN_PROCESS);
        //update user has course
        this.userHasCourseService.updateUserHasCourse(oldUserHasCourses,course.getUserDetailViewModels(),course.getCourseId());
        //update category list id
        List<CategoryHasCourse> oldCategoryHasCourses = this.categoryHasCourseService
                .getAllByCourseId(course.getCourseId());
        this.categoryHasCourseService.UpdateCategoryHasCourse(oldCategoryHasCourses,course.getListCategoryIds(),course.getCourseId());
    }

    public boolean isExist(long courseId){
        return this.courseRepository.existsById(courseId);
    }
    public long getAuthorIdByCourseId(long courseId){
        return this.courseRepository.findAuthorIdByCourseId(courseId);
    }
    //End Pulbic method

    //Private method
    private PagedList<CoursePaginationViewModel> fillDataToPaginationCustom(List<Object[]> rawData,long totalElements,int pageSize){
        long totalPages = (totalElements / pageSize) + 1;
        List<CoursePaginationViewModel> data = ManualCastUtils.castListObjectToCourseFromGetCoursePaginations(rawData);
        return new PagedList<CoursePaginationViewModel>(Math.toIntExact(totalPages),totalElements,data);
    }

    private boolean callStoreProcedureCheckPermission(long courseId,long userId){
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("checkPermissionUserCourse");
        storedProcedureQuery.setParameter("userId",userId);
        storedProcedureQuery.setParameter("courseId",courseId);
        storedProcedureQuery.setParameter("hasPermission",true);

        storedProcedureQuery.execute();

        return Boolean.parseBoolean(storedProcedureQuery.getOutputParameterValue("hasPermission").toString());
    }

    //Public method
}
