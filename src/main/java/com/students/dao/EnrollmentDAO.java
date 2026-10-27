package com.students.dao;

import java.sql.SQLException;
import java.util.List;

public interface EnrollmentDAO {
    boolean enrollStudent(int studentId, int courseId)
            throws SQLException;

    boolean unenrollStudent(int studentId, int courseId)
            throws SQLException;

    List<String> getEnrolledCourses(int studentId)
            throws SQLException;
}
