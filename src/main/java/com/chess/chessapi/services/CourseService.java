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
    private NotificationRepository notificationRepository;

    @Autowired
    private UserHasCourseService userHasCourseService;

    @Autowired
    private CategoryHasCourseService categoryHasCourseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LessonService lessonService;
    //Public method
    public Course create(CourseCreateViewModel courseCreateViewModel, long userId){
        //default setting when created course is inactive
        courseCreateViewModel.setCreatedDate(TimeUtils.getCurrentTime());
        courseCreateViewModel.setStatusId(Status.COURSE_STATUS_DRAFTED);
        courseCreateViewModel.setPoint(Float.parseFloat("0"));
        Course course = ManualCastUtils.castCourseCreateViewModelToCourse(courseCreateViewModel);
        //manual mapping author
        User user = new User();
        user.setUserId(userId);
        course.setUser(user);
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
        storedProcedureQuery.setParameter("statusId",statusId);
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
            course.setUserDetailViewModels(this.userService.getUserDetailsByCourseId(course.getCourseId()));
            course.setListCategoryIds(this.categoryService.getListCategoryIdsByCourseId(course.getCourseId()));

            //check permission only learner join this course can view the detail
            if(this.checkPermissionViewCourseDetail(course)){
                course.setLessonViewModels(this.lessonService.getLessonByCourseId(course.getCourseId()));
            }
        }
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

    public List<CourseDetailViewModel> getCourseDetailsByCategoryId(long categoryId){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCourseByCategoryId");
        query.setParameter("categoryId",categoryId);

        query.execute();
        return ManualCastUtils.castListObjectToCourseDetailsFromGetCourseByCategoryId(query.getResultList());
    }

    public UserDetailViewModel getAuthor(Course course){
        if(course != null){
            if(course.getUserDetailViewModels() == null){
                course.setUserDetailViewModels(this.userService.getUserDetailsByCourseId(course.getCourseId()));
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
                course.setUserDetailViewModels(this.userService.getUserDetailsByCourseId(course.getCourseId()));
            }
            UserPrincipal userPrincipal = userService.getCurrentUser();

            //allow admin permission view course
            if(Long.parseLong(userPrincipal.getRole()) == AppRole.ROLE_ADMIN){
                return true;
            }

            return this.callStoreProcedureCheckPermission(course.getCourseId(),userPrincipal.getId());
        }
        return false;
    }

    public boolean checkPermissionModifyCourse(long courseId){
        //check current user has this course
        UserPrincipal userPrincipal = this.userService.getCurrentUser();

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
