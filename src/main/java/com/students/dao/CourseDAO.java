package com.students.dao;

import com.students.entity.Course;

import java.sql.SQLException;
import java.util.List;

public interface CourseDAO {
    int addCourse(Course course) throws SQLException;
    Course getCourseById(int id) throws SQLException;
    Course getCourseByName(String courseName) throws SQLException;
    List<Course> getAllCourses() throws SQLException;
    boolean updateCourse(Course course) throws SQLException;
    boolean deleteCourse(int id) throws SQLException;
}
