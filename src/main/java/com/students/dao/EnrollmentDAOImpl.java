
package com.students.dao;

import com.students.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAOImpl implements EnrollmentDAO {

    // Enroll student in a course with transaction management
    @Override
    public boolean enrollStudent(int studentId, int courseId)
            throws SQLException {

        String sql = "INSERT INTO enrollments "
                + "(studentId, courseId, enrollmentDate, status) "
                + "VALUES (?, ?, CURDATE(), ?)";

        try (Connection con = DbConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setString(3, "Enrolled");

                int rows = ps.executeUpdate();

                con.commit();

                return rows > 0;

            } catch (SQLException e) {

                // Roll back if enrollment fails
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;

            } finally {
                // Restore the default connection setting
                try {
                    con.setAutoCommit(true);
                } catch (SQLException resetError) {
                    // Connection will be closed by try-with-resources
                }
            }
        }
    }

    // Remove student enrollment
    @Override
    public boolean unenrollStudent(int studentId, int courseId)
            throws SQLException {

        String sql = "DELETE FROM enrollments "
                + "WHERE studentId = ? AND courseId = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            return ps.executeUpdate() > 0;
        }
    }

    // Retrieve all courses enrolled by a student
    @Override
    public List<String> getEnrolledCourses(int studentId)
            throws SQLException {

        List<String> courses = new ArrayList<>();

        String sql = "SELECT c.courseName "
                + "FROM courses c "
                + "JOIN enrollments e "
                + "ON c.courseId = e.courseId "
                + "WHERE e.studentId = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    courses.add(rs.getString("courseName"));
                }
            }
        }

        return courses;
    }

    // Update enrollment status
    public boolean updateEnrollmentStatus(
            int studentId, int courseId, String status)
            throws SQLException {

        String sql = "UPDATE enrollments SET status = ? "
                + "WHERE studentId = ? AND courseId = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, studentId);
            ps.setInt(3, courseId);

            return ps.executeUpdate() > 0;
        }
    }
}
