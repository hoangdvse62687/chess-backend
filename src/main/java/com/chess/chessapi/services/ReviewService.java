package com.chess.chessapi.services;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.constants.AppRole;
import com.chess.chessapi.constants.ObjectType;
import com.chess.chessapi.entities.Course;
import com.chess.chessapi.entities.Review;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.repositories.ReviewRepository;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.CourseOverviewViewModel;
import com.chess.chessapi.viewmodels.ReviewCreateViewModel;
import com.chess.chessapi.viewmodels.ReviewPaginationViewModel;
import com.chess.chessapi.viewmodels.ReviewUpdateViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CourseService courseService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private NotificationService notificationService;

    //PUBLIC DEFINED
    public PagedList<ReviewPaginationViewModel> getReviewPaginationByCourse(int pageIndex,int pageSize,long courseId)
    throws NumberFormatException{
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("getReviewByCourseid");
        storedProcedureQuery.setParameter("pageIndex",(pageIndex - 1) * pageSize);
        storedProcedureQuery.setParameter("pageSize",pageSize);
        storedProcedureQuery.setParameter("courseId",courseId);
        storedProcedureQuery.setParameter("totalElements",Long.parseLong("0"));


        storedProcedureQuery.execute();
        List<Object[]> rawData = storedProcedureQuery.getResultList();
        final long totalElements = Long.parseLong(storedProcedureQuery.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
    }

    public long create(ReviewCreateViewModel reviewCreateViewModel,long userId,String userName){
        Review review = new Review();
        review.setContent(reviewCreateViewModel.getContent());
        review.setCreatedDate(TimeUtils.getCurrentTime());
        review.setRating(reviewCreateViewModel.getRating());
        Course course = new Course();
        course.setCourseId(reviewCreateViewModel.getCourseId());
        review.setCourse(course);

        User user = new User();
        user.setUserId(userId);
        review.setUser(user);

        Review savedReview = this.reviewRepository.save(review);

        long authorId = this.courseService.getAuthorIdByCourseId(reviewCreateViewModel.getCourseId());
        this.notificationService.sendNotificationToUser(AppMessage.NOTIFICATION_REVIEW,userName,ObjectType.REVIEW,
                savedReview.getReviewId(),authorId,AppRole.ROLE_INSTRUCTOR);
        return savedReview.getReviewId();
    }

    public void update(ReviewUpdateViewModel reviewUpdateViewModel){
        this.reviewRepository.update(reviewUpdateViewModel.getReviewId(),reviewUpdateViewModel.getContent(),reviewUpdateViewModel.getRating());
    }

    public void remove(long reviewId){
        this.reviewRepository.remove(reviewId);
    }

    public boolean checkPermissionModifyReview(long reviewId,long userId,long courseId){
        long authorId = this.courseService.getAuthorIdByCourseId(courseId);
        if(authorId == userId){
            return true;
        }
        if(this.reviewRepository.checkPermissionModifyReview(reviewId,userId)){
            return true;
        }
        return false;
    }

    public CourseOverviewViewModel getCourseOverview(long courseId){
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("getOverviewByCourseid");
        storedProcedureQuery.setParameter("courseId",courseId);

        storedProcedureQuery.execute();
        return ManualCastUtils.castObjectToCourseOverviewViewModel(storedProcedureQuery.getResultList());
    }

    public boolean isExist(long reviewId){
        return this.reviewRepository.existsById(reviewId);
    }
    //END PUBLIC DEFINED

    //PRIVATE DEFINED
    private PagedList<ReviewPaginationViewModel> fillDataToPaginationCustom(List<Object[]> rawData,long totalElements,int pageSize){
        long totalPages = (totalElements / pageSize) + 1;
        List<ReviewPaginationViewModel> data = ManualCastUtils.castListObjectToReviewFromGetReview(rawData);
        return new PagedList<ReviewPaginationViewModel>(Math.toIntExact(totalPages),totalElements,data);
    }
    //PRIVATE DEFINED END
}
