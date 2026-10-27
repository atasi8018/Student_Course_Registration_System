package com.students.dao;

import com.students.entity.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentDAO {
    int addStudent(Student student) throws SQLException;
    int[] addStudentsBatch(List<Student> students)
            throws SQLException;
    Student getStudentById(int id) throws SQLException;
    Student getStudentByEmail(String email) throws SQLException;
    List<Student> getAllStudents() throws SQLException;
    boolean updateStudent(Student student) throws SQLException;
    boolean deleteStudent(int id) throws SQLException;
}
