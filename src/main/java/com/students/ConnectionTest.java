package com.students;

import com.students.util.DbConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {

    public static void main(String[] args) {

        try (Connection con = DbConnection.getConnection()) {

            if (con != null && !con.isClosed()) {
                System.out.println("Database connected successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}