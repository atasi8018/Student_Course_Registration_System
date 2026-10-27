package com.students.dao;

import com.students.entity.Student;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.CallableStatement;

public class StudentDAOImpl implements StudentDAO {

    private final DataSource dataSource;

    public StudentDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("studentId"),
                rs.getString("studentName"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }

    // Add student
    @Override
    public int addStudent(Student s) throws SQLException {

        String sql = "{call addStudent(?, ?, ?, ?)}";

        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);

            try (CallableStatement cs = c.prepareCall(sql)) {

                cs.setString(1, s.getStudentName());
                cs.setString(2, s.getEmail());
                cs.setString(3, s.getPhone());

                cs.registerOutParameter(4, java.sql.Types.INTEGER);

                cs.execute();

                int studentId = cs.getInt(4);

                c.commit();

                return studentId;

            } catch (SQLException e) {
                try {
                    c.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;

            } finally {
                try {
                    c.setAutoCommit(true);
                } catch (SQLException resetError) {
                    // Connection closes automatically.
                }
            }
        }
    }

    @Override
    public int[] addStudentsBatch(List<Student> students)
            throws SQLException {

        String sql = "INSERT INTO students(studentName, email, phone) "
                + "VALUES (?, ?, ?)";

        try (Connection c = dataSource.getConnection()) {

            c.setAutoCommit(false);

            try (PreparedStatement ps = c.prepareStatement(sql)) {

                for (Student s : students) {
                    ps.setString(1, s.getStudentName());
                    ps.setString(2, s.getEmail());
                    ps.setString(3, s.getPhone());

                    ps.addBatch();
                }

                int[] results = ps.executeBatch();

                c.commit();

                return results;

            } catch (SQLException e) {

                try {
                    c.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }

    // Get student by ID
    @Override
    public Student getStudentById(int id) throws SQLException {

        String sql = "SELECT * FROM students WHERE studentId = ?";

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    // Get student by email
    @Override
    public Student getStudentByEmail(String email) throws SQLException {

        String sql = "SELECT * FROM students WHERE email = ?";

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    // Get all students
    @Override
    public List<Student> getAllStudents() throws SQLException {

        List<Student> list = new ArrayList<>();

        String sql = "{call getAllStudents()}";

        try (Connection c = dataSource.getConnection();
             CallableStatement cs = c.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list;
    }

    // Update student
    @Override
    public boolean updateStudent(Student s) throws SQLException {

        String sql = "UPDATE students "
                + "SET studentName = ?, email = ?, phone = ? "
                + "WHERE studentId = ?";

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getStudentName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPhone());
            ps.setInt(4, s.getStudentId());

            return ps.executeUpdate() > 0;
        }
    }

    // Delete student
    @Override
    public boolean deleteStudent(int id) throws SQLException {

        String sql = "DELETE FROM students WHERE studentId = ?";

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
}
