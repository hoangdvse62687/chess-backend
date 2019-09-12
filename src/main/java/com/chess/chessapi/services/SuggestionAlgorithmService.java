package com.chess.chessapi.services;

import com.chess.chessapi.constants.EloRatingLevel;
import com.chess.chessapi.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SuggestionAlgorithmService {
    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RedisCourseSuggestionService redisCourseSuggestionService;

    public void executeSuggestionAlgorithm(){
        SuggestionAlgorithmData suggestionAlgorithmData = new SuggestionAlgorithmData();
        //init data for start algorithm
        suggestionAlgorithmData.setAllUserBeginner(this.getListUserIdByRangeElo(0,EloRatingLevel.BEGINNER_ELO));
        suggestionAlgorithmData.setAllUserMinor(this.getListUserIdByRangeElo(EloRatingLevel.BEGINNER_ELO,EloRatingLevel.MINOR_ELO));
        suggestionAlgorithmData.setAllUserIntermediate(this.getListUserIdByRangeElo(EloRatingLevel.MINOR_ELO,EloRatingLevel.INTERMEDIATE_ELO));
        suggestionAlgorithmData.setAllUserMajor(this.getListUserIdByRangeElo(EloRatingLevel.INTERMEDIATE_ELO,EloRatingLevel.MAJOR_ELO));
        suggestionAlgorithmData.setAllUserMaster(this.getListUserIdByRangeElo(EloRatingLevel.MAJOR_ELO,EloRatingLevel.MASTER_ELO));

        suggestionAlgorithmData.setAllCourseBeginner(this.courseService.getListCoursePulishedIdsByEloId(EloRatingLevel.BEGINNER_ID));
        suggestionAlgorithmData.setAllCourseMinor(this.courseService.getListCoursePulishedIdsByEloId(EloRatingLevel.MINOR_ID));
        suggestionAlgorithmData.setAllCourseIntermediate(this.courseService.getListCoursePulishedIdsByEloId(EloRatingLevel.INTERMEDIATE_ID));
        suggestionAlgorithmData.setAllCourseMajor(this.courseService.getListCoursePulishedIdsByEloId(EloRatingLevel.MAJOR_ID));
        suggestionAlgorithmData.setAllCourseMaster(this.courseService.getListCoursePulishedIdsByEloId(EloRatingLevel.MASTER_ID));

        this.getReviews(suggestionAlgorithmData.getAllUserBeginner(),suggestionAlgorithmData.getAllCourseBeginner());
        this.getReviews(suggestionAlgorithmData.getAllUserMinor(),suggestionAlgorithmData.getAllCourseMinor());
        this.getReviews(suggestionAlgorithmData.getAllUserIntermediate(),suggestionAlgorithmData.getAllCourseIntermediate());
        this.getReviews(suggestionAlgorithmData.getAllUserMajor(),suggestionAlgorithmData.getAllCourseMajor());
        this.getReviews(suggestionAlgorithmData.getAllUserMaster(),suggestionAlgorithmData.getAllCourseMaster());

        //find similar of each user in group
        this.executeCosineSimilarity(suggestionAlgorithmData.getAllUserBeginner());
        this.executeCosineSimilarity(suggestionAlgorithmData.getAllUserMinor());
        this.executeCosineSimilarity(suggestionAlgorithmData.getAllUserIntermediate());
        this.executeCosineSimilarity(suggestionAlgorithmData.getAllUserMajor());
        this.executeCosineSimilarity(suggestionAlgorithmData.getAllUserMaster());

        this.executeUserBasedFiltering(suggestionAlgorithmData.getAllUserBeginner(),suggestionAlgorithmData.getAllCourseBeginner());
        this.executeUserBasedFiltering(suggestionAlgorithmData.getAllUserMinor(),suggestionAlgorithmData.getAllCourseMinor());
        this.executeUserBasedFiltering(suggestionAlgorithmData.getAllUserIntermediate(),suggestionAlgorithmData.getAllCourseIntermediate());
        this.executeUserBasedFiltering(suggestionAlgorithmData.getAllUserMajor(),suggestionAlgorithmData.getAllCourseMajor());
        this.executeUserBasedFiltering(suggestionAlgorithmData.getAllUserMaster(),suggestionAlgorithmData.getAllCourseMaster());

        this.saveOnRedis(suggestionAlgorithmData.getAllUserBeginner());
        this.saveOnRedis(suggestionAlgorithmData.getAllUserMinor());
        this.saveOnRedis(suggestionAlgorithmData.getAllUserIntermediate());
        this.saveOnRedis(suggestionAlgorithmData.getAllUserMajor());
        this.saveOnRedis(suggestionAlgorithmData.getAllUserMaster());
    }

    private void saveOnRedis(List<UserSuggestionAlgorithm> suggestionAlgorithmData){
        suggestionAlgorithmData.forEach((user) -> {
            CourseSuggestionRedis courseSuggestionRedis = new CourseSuggestionRedis();
            courseSuggestionRedis.setUserId(user.getUserId());
            Collections.sort(user.getUserFilterScore());
            courseSuggestionRedis.setCourseUserFilterData(user.getUserFilterScore());
            this.redisCourseSuggestionService.save(courseSuggestionRedis);
        });
    }

    private void executeCosineSimilarity(List<UserSuggestionAlgorithm> userSuggestionAlgorithms){
        for(int i = 0;i < userSuggestionAlgorithms.size();i++){
            for(int j = 0; j < userSuggestionAlgorithms.size();j++){
                if(i != j){
                    double result = this.cosineSimilarity(userSuggestionAlgorithms.get(i).getReviewSuggestionAlgorithms()
                            ,userSuggestionAlgorithms.get(j).getReviewSuggestionAlgorithms());
                    userSuggestionAlgorithms.get(i).getUserSimilarSuggestionAlgorithms()
                            .add(new UserSimilarSuggestionAlgorithm(userSuggestionAlgorithms.get(j).getUserId(),result));
                }
            }
        }
    }

    private void executeUserBasedFiltering(List<UserSuggestionAlgorithm> userSuggestionAlgorithms,List<Long> courseIds){
        for(int i = 0; i < userSuggestionAlgorithms.size();i++){
            for(int j = 0; j < courseIds.size();j++){
                List<Double> listRatingOfUser = new ArrayList<>();
                for (int z = 0; z < userSuggestionAlgorithms.size();z++){
                    if(z != i){
                        listRatingOfUser.add(userSuggestionAlgorithms.get(z).getReviewSuggestionAlgorithms().get(j).getScore());
                    }
                }
                double result = this.userBasedFiltering(listRatingOfUser
                        ,userSuggestionAlgorithms.get(i).getUserSimilarSuggestionAlgorithms());
                userSuggestionAlgorithms.get(i).getUserFilterScore().add(new CourseUserFilterData(courseIds.get(j),result));
            }
        }
    }

    private void getReviews(List<UserSuggestionAlgorithm> userSuggestionAlgorithms,List<Long> courseIds){
        userSuggestionAlgorithms.forEach((user) ->{
            courseIds.forEach((courseId) -> {
                user.getReviewSuggestionAlgorithms().add(new CourseUserFilterData(courseId
                        ,Double.parseDouble(this.reviewService.getRatingByUserIdAndCourseId(user.getUserId(),courseId).toString())));
            });
        });
    }

    private List<UserSuggestionAlgorithm> getListUserIdByRangeElo(int minElo,int maxElo){
        List<UserSuggestionAlgorithm> users = new ArrayList<>();
        List<Long> listUsers = this.userService.getListLearnerByRangeElo(minElo, maxElo);
        listUsers.forEach((item) ->{
            users.add(new UserSuggestionAlgorithm(item));
        });
        return users;
    }

    private double cosineSimilarity(List<CourseUserFilterData> A,List<CourseUserFilterData> B) {
        //A is user a and B is user b
        if (A == null || B == null || A.size() == 0 || B.size() == 0 || A.size() != B.size()) {
            return 0;
        }

        double sumProduct = 0;
        double sumASq = 0;
        double sumBSq = 0;
        for (int i = 0; i < A.size(); i++) {
            sumProduct += A.get(i).getScore()*B.get(i).getScore();
            sumASq += A.get(i).getScore() * A.get(i).getScore();
            sumBSq += B.get(i).getScore() * B.get(i).getScore();
        }
        if (sumASq == 0) {
            sumASq = 1;
        }
        if(sumBSq == 0){
            sumBSq = 1;
        }
        return sumProduct / (Math.sqrt(sumASq) * Math.sqrt(sumBSq));
    }

    private double userBasedFiltering(List<Double> A,List<UserSimilarSuggestionAlgorithm> B){
        //A is rating of user and B is similar of user
        if(A == null || B == null || A.size() == 0 || B.size() == 0 || A.size() != B.size()){
            return 0;
        }
        double sumProduct = 0;
        double sumSimilar = 0;
        for(int i = 0; i < A.size(); i++){
            sumProduct += A.get(i)*B.get(i).getSimilarScore();
            sumSimilar += B.get(i).getSimilarScore();
        }
        if(sumSimilar == 0){
            return 0;
        }
        return sumProduct / sumSimilar;
    }
}
