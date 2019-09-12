package com.chess.chessapi.models;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAlgorithmData {
    private List<UserSuggestionAlgorithm> allUserBeginner;
    private List<UserSuggestionAlgorithm> allUserMinor;
    private List<UserSuggestionAlgorithm> allUserIntermediate;
    private List<UserSuggestionAlgorithm> allUserMajor;
    private List<UserSuggestionAlgorithm> allUserMaster;

    private List<Long> allCourseBeginner;
    private List<Long> allCourseMinor;
    private List<Long> allCourseIntermediate;
    private List<Long> allCourseMajor;
    private List<Long> allCourseMaster;

    public List<UserSuggestionAlgorithm> getAllUserBeginner() {
        if(this.allUserBeginner == null){
            this.allUserBeginner = new ArrayList<>();
        }
        return allUserBeginner;
    }

    public void setAllUserBeginner(List<UserSuggestionAlgorithm> allUserBeginner) {
        this.allUserBeginner = allUserBeginner;
    }

    public List<UserSuggestionAlgorithm> getAllUserMinor() {
        return allUserMinor;
    }

    public void setAllUserMinor(List<UserSuggestionAlgorithm> allUserMinor) {
        this.allUserMinor = allUserMinor;
    }

    public List<UserSuggestionAlgorithm> getAllUserIntermediate() {
        return allUserIntermediate;
    }

    public void setAllUserIntermediate(List<UserSuggestionAlgorithm> allUserIntermediate) {
        this.allUserIntermediate = allUserIntermediate;
    }

    public List<UserSuggestionAlgorithm> getAllUserMajor() {
        return allUserMajor;
    }

    public void setAllUserMajor(List<UserSuggestionAlgorithm> allUserMajor) {
        this.allUserMajor = allUserMajor;
    }

    public List<UserSuggestionAlgorithm> getAllUserMaster() {
        return allUserMaster;
    }

    public void setAllUserMaster(List<UserSuggestionAlgorithm> allUserMaster) {
        this.allUserMaster = allUserMaster;
    }

    public List<Long> getAllCourseBeginner() {
        return allCourseBeginner;
    }

    public void setAllCourseBeginner(List<Long> allCourseBeginner) {
        this.allCourseBeginner = allCourseBeginner;
    }

    public List<Long> getAllCourseMinor() {
        return allCourseMinor;
    }

    public void setAllCourseMinor(List<Long> allCourseMinor) {
        this.allCourseMinor = allCourseMinor;
    }

    public List<Long> getAllCourseIntermediate() {
        return allCourseIntermediate;
    }

    public void setAllCourseIntermediate(List<Long> allCourseIntermediate) {
        this.allCourseIntermediate = allCourseIntermediate;
    }

    public List<Long> getAllCourseMajor() {
        return allCourseMajor;
    }

    public void setAllCourseMajor(List<Long> allCourseMajor) {
        this.allCourseMajor = allCourseMajor;
    }

    public List<Long> getAllCourseMaster() {
        return allCourseMaster;
    }

    public void setAllCourseMaster(List<Long> allCourseMaster) {
        this.allCourseMaster = allCourseMaster;
    }
}
