package com.students.entity;

import java.math.BigDecimal;

public class Course {
    private int courseId;
    private String courseName;
    private String courseCode;
    private BigDecimal fee;

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", fee=" + fee +
                '}';
    }

    public Course(int courseId, String courseName, String courseCode, BigDecimal fee) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.fee = fee;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Course(){

    }
}
