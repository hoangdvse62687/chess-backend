package com.chess.chessapi.services;

import com.chess.chessapi.constants.*;
import com.chess.chessapi.entities.Course;
import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.repositories.CourseRepository;
import com.chess.chessapi.repositories.NotificationRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.CoursePaginationViewModel;
import com.chess.chessapi.viewmodels.InteractiveLessonViewModel;
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
    private NotificationRepository notificationRepository;

    //Public method
    public Course create(Course course){
        //default setting when created course is inactive
        course.setCreatedDate(TimeUtils.getCurrentTime());
        course.setStatusId(Status.COURSE_STATUS_DRAFTED);
        course.setPoint(Float.parseFloat("0"));
        Course savedCourse = courseRepository.save(course);

        //Send notification to Admin
        Notification notification = new Notification();
        notification.setObjectTypeId(ObjectType.COURSE);
        notification.setObjectName(savedCourse.getName());
        notification.setObjectId(savedCourse.getCourseId());
        notification.setContent(AppMessage.CREATE_NEW_COURSE);
        notification.setCreateDate(TimeUtils.getCurrentTime());
        notification.setViewed(false);
        notification.setRoleTarget(AppRole.ROLE_ADMIN);
        this.notificationRepository.save(notification);

        return savedCourse;
    }

    public PagedList<CoursePaginationViewModel> getCoursePaginationByStatusId(String courseName,int pageIndex, int pageSize, String statusId)
            throws NumberFormatException{
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("getCoursePaginations");
        storedProcedureQuery.setParameter("courseName",courseName);
        storedProcedureQuery.setParameter("pageIndex",(pageIndex - 1) * pageSize);
        storedProcedureQuery.setParameter("pageSize",pageSize);
        storedProcedureQuery.setParameter("status_id",statusId);
        storedProcedureQuery.setParameter("role_id",AppRole.ROLE_INSTRUCTOR);
        storedProcedureQuery.setParameter("totalElements",Long.parseLong("0"));


        storedProcedureQuery.execute();

        List<Object[]> rawData = storedProcedureQuery.getResultList();
        final long totalElements = Long.parseLong(storedProcedureQuery.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
    }

    public void updateStatus(long courseId,long statusId){
        this.courseRepository.updateStatus(courseId,statusId);
    }

    public Optional<Course> getCourseById(long id){
        return this.courseRepository.findById(id);
    }

    public void getCourseDetails(Course course){
        if(course != null){
            course.setUserDetailViewModels(this.getUserDetails(course.getCourseId()));
            course.setListCategoryIds(this.getListCategoryIds(course.getCourseId()));

            //check permission only learner join this course can view the detail
            if(this.checkPermissionViewCourseDetail(course)){
                course.setInteractiveLessonViewModels(this.getInteractiveLessonDetails(course.getCourseId()));
            }
        }
    }

    public List<UserDetailViewModel> getUserDetails(long courseId){
        //getting users by courseid
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getUsersByCourseid");
        query.setParameter("courseId",courseId);

        query.execute();

        //end getting users by courseid
        return ManualCastUtils.castListObjectToUserDetailsFromGetUsersByCourseid(query.getResultList());
    }

    public List<Long> getListCategoryIds(long courseId){
        //getting category by courseid
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCategoryByCourseid");
        query.setParameter("courseId",courseId);

        query.execute();
        //end getting category by courseid
        return ManualCastUtils.castListObjectToCategoryIdFromGetCategoryByCourseId(query.getResultList());
    }

    public List<InteractiveLessonViewModel> getInteractiveLessonDetails(long courseId){
        //getting interactive by courseid
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getInteractiveLessonByCourseId");
        query.setParameter("courseId",courseId);

        query.execute();
        //end getting interactive by courseid
        return ManualCastUtils.castListObjectToInteractiveLessonDetails(query.getResultList());
    }

    public UserDetailViewModel getAuthor(Course course){
        if(course != null){
            if(course.getUserDetailViewModels() == null){
                course.setUserDetailViewModels(this.getUserDetails(course.getCourseId()));
            }
            UserDetailViewModel author = null;
            for (UserDetailViewModel userDetailViewModel:
                    course.getUserDetailViewModels()) {
                if(userDetailViewModel.getRoleId() == AppRole.ROLE_INSTRUCTOR){
                    author = userDetailViewModel;
                    break;
                }
            }
            return author;
        }
        return null;
    }

    public boolean checkPermissionViewCourseDetail(Course course){
        if(course != null){
            if(course.getUserDetailViewModels() == null){
                course.setUserDetailViewModels(this.getUserDetails(course.getCourseId()));
            }
            UserPrincipal userPrincipal = userService.getCurrentUser();

            if(userPrincipal == null){
                return false;
            }

            for (UserDetailViewModel userDetailViewModel:
                    course.getUserDetailViewModels()) {
                if(userDetailViewModel.getUserId() == userPrincipal.getId()){
                    return true;
                }
            }
        }
        return false;
    }
    //End Pulbic method

    //Private method
    private PagedList<CoursePaginationViewModel> fillDataToPaginationCustom(List<Object[]> rawData,long totalElements,int pageSize){
        long totalPages = (totalElements / pageSize) + 1;
        List<CoursePaginationViewModel> data = ManualCastUtils.castListObjectToCourseFromGetCoursePaginations(rawData);
        return new PagedList<CoursePaginationViewModel>(Math.toIntExact(totalPages),totalElements,data);
    }
    //Public method
}
