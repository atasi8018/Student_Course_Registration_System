package com.students.service;

import com.students.dao.CourseDAO;
import com.students.entity.Course;

import java.sql.SQLException;
import java.util.List;

public class CourseService {

    private final CourseDAO dao;

    public CourseService(CourseDAO dao) {
        this.dao = dao;
    }

    // Add a new course
    public int addCourse(Course course) throws SQLException {

        validateCourse(course);

        Course existing = dao.getCourseByName(
                course.getCourseName().trim());

        if (existing != null) {
            throw new IllegalArgumentException(
                    "Course name already exists");
        }

        return dao.addCourse(course);
    }

    // Find course by ID
    public Course getCourseDetails(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Course ID must be positive");
        }

        return dao.getCourseById(id);
    }

    // Find course by name
    public Course getCourseByName(String courseName)
            throws SQLException {

        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Course name cannot be empty");
        }

        return dao.getCourseByName(courseName.trim());
    }

    // Display all courses
    public List<Course> getAllCourses() throws SQLException {
        return dao.getAllCourses();
    }

    // Update course
    public void updateCourse(Course course) throws SQLException {

        validateCourse(course);

        if (course.getCourseId() <= 0) {
            throw new IllegalArgumentException(
                    "Course ID must be positive");
        }

        Course existing = dao.getCourseByName(
                course.getCourseName().trim());

        if (existing != null
                && existing.getCourseId() != course.getCourseId()) {
            throw new IllegalArgumentException(
                    "Course name already exists");
        }

        boolean updated = dao.updateCourse(course);

        if (!updated) {
            throw new IllegalArgumentException(
                    "Course not found");
        }
    }

    // Delete course
    public void deleteCourse(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Course ID must be positive");
        }

        boolean deleted = dao.deleteCourse(id);

        if (!deleted) {
            throw new IllegalArgumentException(
                    "Course not found");
        }
    }

    // Validate course details
    private void validateCourse(Course course) {

        if (course == null) {
            throw new IllegalArgumentException(
                    "Course cannot be null");
        }

        if (course.getCourseName() == null
                || course.getCourseName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Course name is required");
        }

        if (course.getCourseCode() == null
                || course.getCourseCode().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Course code is required");
        }

        if (course.getFee() == null
                || course.getFee().signum() < 0) {
            throw new IllegalArgumentException(
                    "Course fee cannot be negative");
        }
    }
}