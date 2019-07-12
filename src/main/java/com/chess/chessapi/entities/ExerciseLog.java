package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "exercise_log")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="exerciseLogId",scope = ExerciseLog.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getLogExerciseidByUseridAndCourseid",
                procedureName = "get_log_exerciseid_by_userid_and_courseid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "userId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "courseId",type = Long.class)
                }
        )
})
public class ExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long exerciseLogId;

    @Column(name = "is_passed")
    private boolean isPassed;

    @Column(name = "created_date")
    @NotNull(message = "Created Date is required not null")
    private Timestamp createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="exerise_id")
    @JsonIgnore
    private Exercise exercise;

    public long getExerciseLogId() {
        return exerciseLogId;
    }

    public void setExerciseLogId(long exerciseLogId) {
        this.exerciseLogId = exerciseLogId;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
