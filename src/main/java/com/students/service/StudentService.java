
package com.students.service;

import com.students.dao.StudentDAO;
import com.students.entity.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {

    private final StudentDAO dao;

    public StudentService(StudentDAO dao) {
        this.dao = dao;
    }

    // Register a new student
    public int registerStudent(Student student) throws SQLException {

        validateStudent(student);

        Student existing = dao.getStudentByEmail(
                student.getEmail().trim());

        if (existing != null) {
            throw new IllegalArgumentException(
                    "Email address already exists");
        }

        return dao.addStudent(student);
    }
    public int[] registerStudentsBatch(List<Student> students)
            throws SQLException {

        if (students == null || students.isEmpty()) {
            throw new IllegalArgumentException(
                    "Student list cannot be empty");
        }

        for (Student student : students) {
            if (student == null
                    || student.getStudentName() == null
                    || student.getStudentName().trim().isEmpty()
                    || student.getEmail() == null
                    || student.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Student name and email are required");
            }
        }

        return dao.addStudentsBatch(students);
    }

    // Find student by ID
    public Student getStudentById(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive");
        }

        return dao.getStudentById(id);
    }

    // Find student by email
    public Student getStudentByEmail(String email)
            throws SQLException {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Email cannot be empty");
        }

        return dao.getStudentByEmail(email.trim());
    }

    // Display all students
    public List<Student> getAllStudents() throws SQLException {
        return dao.getAllStudents();
    }

    // Update student details
    public void updateStudent(Student student) throws SQLException {

        validateStudent(student);

        if (student.getStudentId() <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive");
        }

        Student existing = dao.getStudentByEmail(
                student.getEmail().trim());

        if (existing != null
                && existing.getStudentId() != student.getStudentId()) {
            throw new IllegalArgumentException(
                    "Email address already belongs to another student");
        }

        boolean updated = dao.updateStudent(student);

        if (!updated) {
            throw new IllegalArgumentException(
                    "Student not found or no changes made");
        }
    }

    // Delete student
    public void deleteStudent(int id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be positive");
        }

        boolean deleted = dao.deleteStudent(id);

        if (!deleted) {
            throw new IllegalArgumentException(
                    "Student not found");
        }
    }

    // Validate student details
    private void validateStudent(Student student) {

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student cannot be null");
        }

        if (student.getStudentName() == null
                || student.getStudentName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Student name is required");
        }

        if (student.getEmail() == null
                || !student.getEmail().trim().matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Enter a valid email address");
        }

        if (student.getPhone() != null
                && !student.getPhone().trim().isEmpty()
                && !student.getPhone().trim().matches("[0-9+() -]{7,20}")) {
            throw new IllegalArgumentException(
                    "Enter a valid phone number");
        }
    }
}
