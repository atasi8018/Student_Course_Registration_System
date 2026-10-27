package com.students.entity;

import java.time.LocalDate;

public class StudentCourseReport {
    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    @Override
    public String toString() {
        return "StudentCourseReport{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }

    private final int studentId;
    private final String studentName;
    private final int courseId;
    private final String courseName;
    private final LocalDate enrollmentDate;

    public StudentCourseReport(int studentId, String studentName, int courseId, String courseName, LocalDate enrollmentDate) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.enrollmentDate = enrollmentDate;
    }

}
