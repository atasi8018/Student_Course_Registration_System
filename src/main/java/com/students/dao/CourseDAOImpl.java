
package com.students.dao;

import com.students.entity.Course;
import com.students.util.DbConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {

    public CourseDAOImpl(DataSource dataSource) {
    }

    // Add course
    @Override
    public int addCourse(Course course) throws SQLException {

        String sql = "INSERT INTO courses "
                + "(courseName, courseCode, fee) VALUES (?, ?, ?)";

        try (Connection con = DbConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                    sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, course.getCourseName());
                ps.setString(2, course.getCourseCode());
                ps.setBigDecimal(3, course.getFee());

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int courseId = rs.getInt(1);
                            con.commit();
                            return courseId;
                        }
                    }
                }

                con.rollback();
                return 0;

            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            } finally {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException resetError) {
                    // Connection closes automatically.
                }
            }
        }
    }

    // Search course by ID
    @Override
    public Course getCourseById(int courseId) throws SQLException {

        String sql = "SELECT * FROM courses WHERE courseId = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCourse(rs);
                }
            }
        }

        return null;
    }

    // Search course by name
    @Override
    public Course getCourseByName(String courseName)
            throws SQLException {

        String sql = "SELECT * FROM courses WHERE courseName = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCourse(rs);
                }
            }
        }

        return null;
    }

    // Display all courses
    @Override
    public List<Course> getAllCourses() throws SQLException {

        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                courses.add(mapCourse(rs));
            }
        }

        return courses;
    }

    // Update course
    @Override
    public boolean updateCourse(Course course) throws SQLException {

        String sql = "UPDATE courses SET courseName = ?, "
                + "courseCode = ?, fee = ? WHERE courseId = ?";

        try (Connection con = DbConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, course.getCourseName());
                ps.setString(2, course.getCourseCode());
                ps.setBigDecimal(3, course.getFee());
                ps.setInt(4, course.getCourseId());

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }

            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            } finally {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException resetError) {
                    // Connection closes automatically.
                }
            }
        }
    }

    // Delete course
    @Override
    public boolean deleteCourse(int courseId) throws SQLException {

        String sql = "DELETE FROM courses WHERE courseId = ?";

        try (Connection con = DbConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, courseId);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }

            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            } finally {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException resetError) {
                    // Connection closes automatically.
                }
            }
        }
    }

    // Convert database record into Course object
    private Course mapCourse(ResultSet rs) throws SQLException {

        return new Course(
                rs.getInt("courseId"),
                rs.getString("courseName"),
                rs.getString("courseCode"),
                rs.getBigDecimal("fee")
        );
    }
}
