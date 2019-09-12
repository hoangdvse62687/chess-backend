package com.chess.chessapi.models;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CourseSuggestionRedis extends JdkSerializationRedisSerializer implements Serializable {
    private long userId;
    private List<CourseUserFilterData> courseUserFilterData;
    private static final long serialVersionUID = 1L;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<CourseUserFilterData> getCourseUserFilterData() {
        return courseUserFilterData;
    }

    public void setCourseUserFilterData(List<CourseUserFilterData> courseUserFilterData) {
        this.courseUserFilterData = courseUserFilterData;
    }
}
