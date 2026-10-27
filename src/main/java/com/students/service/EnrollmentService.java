
package com.students.service;
import com.students.dao.StudentDAO;
import com.students.dao.CourseDAO;
import com.students.dao.EnrollmentDAO;
import com.students.entity.Student;
import com.students.entity.Course;

import java.sql.SQLException;
import java.util.List;

public class EnrollmentService {

    private final EnrollmentDAO enrollmentDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public EnrollmentService(
            EnrollmentDAO enrollmentDAO,
            StudentDAO studentDAO,
            CourseDAO courseDAO) {

        this.enrollmentDAO = enrollmentDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    // Enroll a student in a course
    public boolean enrollStudent(int studentId, int courseId)
            throws SQLException {

        validateIds(studentId, courseId);

        Student student = studentDAO.getStudentById(studentId);

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student not found");
        }

        Course course = courseDAO.getCourseById(courseId);

        if (course == null) {
            throw new IllegalArgumentException(
                    "Course not found");
        }

        return enrollmentDAO.enrollStudent(studentId, courseId);
    }

    // Withdraw a student from a course
    public boolean unenrollStudent(int studentId, int courseId)
            throws SQLException {

        validateIds(studentId, courseId);

        return enrollmentDAO.unenrollStudent(studentId, courseId);
    }

    // Get all courses enrolled by a student
    public List<String> getEnrolledCourses(int studentId)
            throws SQLException {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive");
        }

        Student student = studentDAO.getStudentById(studentId);

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student not found");
        }

        return enrollmentDAO.getEnrolledCourses(studentId);
    }

    // Validate enrollment IDs
    private void validateIds(int studentId, int courseId) {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive");
        }

        if (courseId <= 0) {
            throw new IllegalArgumentException(
                    "Course ID must be positive");
        }
    }
}
