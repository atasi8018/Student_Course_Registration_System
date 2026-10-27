package com.students;
import com.students.dao.StudentDAO;
import com.students.dao.StudentDAOImpl;
import com.students.dao.CourseDAO;
import com.students.dao.CourseDAOImpl;
import com.students.dao.EnrollmentDAO;
import com.students.dao.EnrollmentDAOImpl;

import com.students.entity.Student;
import com.students.entity.Course;

import com.students.service.StudentService;
import com.students.service.CourseService;
import com.students.service.EnrollmentService;

import com.students.util.DbConnection;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentCourseManagement {

    private static final Scanner sc = new Scanner(System.in);

    private static StudentService studentService;
    private static CourseService courseService;
    private static EnrollmentService enrollmentService;

    public static void main(String[] args) {

        try {
            DataSource dataSource = DbConnection.getDataSource();

            StudentDAO studentDAO = new StudentDAOImpl(dataSource);
            CourseDAO courseDAO = new CourseDAOImpl(dataSource);
            EnrollmentDAO enrollmentDAO = new EnrollmentDAOImpl();


            studentService = new StudentService(studentDAO);
            courseService = new CourseService(courseDAO);


            enrollmentService = new EnrollmentService(
                    enrollmentDAO, studentDAO, courseDAO);

            runMenu();

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());

        } finally {
            sc.close();
        }
    }

    private static void runMenu() {

        boolean running = true;

        while (running) {
            displayMenu();

            try {
                int choice = readInt("Enter your choice: ");

                switch (choice) {
                    case 1:
                        registerStudent();
                        break;
                    case 2:
                        searchStudentById();
                        break;
                    case 3:
                        searchStudentByEmail();
                        break;
                    case 4:
                        displayAllStudents();
                        break;
                    case 5:
                        updateStudent();
                        break;
                    case 6:
                        deleteStudent();
                        break;
                    case 7:
                        addCourse();
                        break;
                    case 8:
                        searchCourseById();
                        break;
                    case 9:
                        displayAllCourses();
                        break;
                    case 10:
                        updateCourse();
                        break;
                    case 11:
                        deleteCourse();
                        break;
                    case 12:
                        enrollStudent();
                        break;
                    case 13:
                        displayEnrolledCourses();
                        break;
                    case 14:
                        unenrollStudent();
                        break;
                    case 15:
                        registerStudentsBatch();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Thank you for using SCMS!");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (SQLException e) {
                System.out.println("Database operation failed: "
                        + e.getMessage());

            } catch (IllegalArgumentException e) {
                System.out.println("Input error: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private static void displayMenu() {

        System.out.println("==========================================");
        System.out.println("     STUDENT COURSE MANAGEMENT SYSTEM");
        System.out.println("==========================================");
        System.out.println("STUDENT MANAGEMENT");
        System.out.println("1. Register Student");
        System.out.println("2. Search Student by ID");
        System.out.println("3. Search Student by Email");
        System.out.println("4. Display All Students");
        System.out.println("5. Update Student");
        System.out.println("6. Delete Student");
        System.out.println("------------------------------------------");
        System.out.println("COURSE MANAGEMENT");
        System.out.println("7. Add Course");
        System.out.println("8. Search Course by ID");
        System.out.println("9. Display All Courses");
        System.out.println("10. Update Course");
        System.out.println("11. Delete Course");
        System.out.println("------------------------------------------");
        System.out.println("ENROLLMENT MANAGEMENT");
        System.out.println("12. Enroll Student in Course");
        System.out.println("13. View Student's Courses");
        System.out.println("14. Withdraw from Course");
        System.out.println("15. Register Students in Batch");
        System.out.println("0. Exit");
        System.out.println("==========================================");
    }

    // Student operations

    private static void registerStudent() throws SQLException {

        System.out.println("--- Register Student ---");

        String studentName = readRequired("Student name: ");
        String email = readRequired("Email: ");
        String phone = readRequired("Phone: ");

        Student student = new Student(0, studentName, email, phone);

        int studentId = studentService.registerStudent(student);

        System.out.println("Student registered successfully. ID: "
                + studentId);
    }

    private static void registerStudentsBatch() throws SQLException {
        System.out.print("How many students do you want to add? ");
        int count = Integer.parseInt(sc.nextLine());

        if (count <= 0) {
            System.out.println("Enter a positive number.");
            return;
        }

        List<Student> students = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            System.out.println("--- Student " + (i + 1) + " ---");

            System.out.print("Student name: ");
            String name = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Phone: ");
            String phone = sc.nextLine();

            students.add(new Student(0, name, email, phone));
        }

        int[] results = studentService.registerStudentsBatch(students);
        System.out.println("Batch registration completed. Results: "
                + results.length + " student operations.");
    }
    private static void searchStudentById() throws SQLException {

        int studentId = readPositiveInt("Enter student ID: ");

        Student student = studentService.getStudentById(studentId);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }

    private static void searchStudentByEmail() throws SQLException {

        String email = readRequired("Enter email: ");

        Student student = studentService.getStudentByEmail(email);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }

    private static void displayAllStudents() throws SQLException {

        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : students) {
            System.out.println(student);
        }

        System.out.println("Total students: " + students.size());
    }

    private static void updateStudent() throws SQLException {

        int studentId = readPositiveInt("Enter student ID to update: ");

        Student existingStudent =
                studentService.getStudentById(studentId);

        if (existingStudent == null) {
            System.out.println("Student not found.");
            return;
        }

        String studentName = readRequired("New student name: ");
        String email = readRequired("New email: ");
        String phone = readRequired("New phone: ");

        Student student = new Student(
                studentId, studentName, email, phone);

        studentService.updateStudent(student);

        System.out.println("Student updated successfully.");
    }

    private static void deleteStudent() throws SQLException {

        int studentId = readPositiveInt("Enter student ID to delete: ");

        System.out.print("Confirm deletion? (yes/no): ");
        String answer = sc.nextLine().trim();

        if (answer.equalsIgnoreCase("yes")) {
            studentService.deleteStudent(studentId);
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Course operations

    private static void addCourse() throws SQLException {

        System.out.println("--- Add Course ---");

        String courseName = readRequired("Course name: ");
        String courseCode = readRequired("Course code: ");
        BigDecimal fee = readFee("Course fee: ");

        Course course = new Course(
                0, courseName, courseCode, fee);

        int courseId = courseService.addCourse(course);

        System.out.println("Course added successfully. ID: "
                + courseId);
    }


    private static void searchCourseById() throws SQLException {

        int courseId = readPositiveInt("Enter course ID: ");

        Course course = courseService.getCourseDetails(courseId);

        if (course == null) {
            System.out.println("Course not found.");
        } else {
            System.out.println(course);
        }
    }

    private static void displayAllCourses() throws SQLException {

        List<Course> courses = courseService.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        for (Course course : courses) {
            System.out.println(course);
        }

        System.out.println("Total courses: " + courses.size());
    }

    private static void updateCourse() throws SQLException {

        int courseId = readPositiveInt("Enter course ID to update: ");

        Course existingCourse =
                courseService.getCourseDetails(courseId);

        if (existingCourse == null) {
            System.out.println("Course not found.");
            return;
        }

        String courseName = readRequired("New course name: ");
        String courseCode = readRequired("New course code: ");
        BigDecimal fee = readFee("New course fee: ");

        Course course = new Course(
                courseId, courseName, courseCode, fee);

        courseService.updateCourse(course);

        System.out.println("Course updated successfully.");
    }

    private static void deleteCourse() throws SQLException {

        int courseId = readPositiveInt("Enter course ID to delete: ");

        System.out.print("Confirm deletion? (yes/no): ");
        String answer = sc.nextLine().trim();

        if (answer.equalsIgnoreCase("yes")) {
            courseService.deleteCourse(courseId);
            System.out.println("Course deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Enrollment operations

    private static void enrollStudent() throws SQLException {

        int studentId = readPositiveInt("Enter student ID: ");
        int courseId = readPositiveInt("Enter course ID: ");

        boolean enrolled =
                enrollmentService.enrollStudent(studentId, courseId);

        System.out.println(enrolled
                ? "Student enrolled successfully."
                : "Enrollment could not be completed.");
    }

    private static void displayEnrolledCourses() throws SQLException {

        int studentId = readPositiveInt("Enter student ID: ");

        List<String> courses =
                enrollmentService.getEnrolledCourses(studentId);

        if (courses.isEmpty()) {
            System.out.println("No enrolled courses found.");
            return;
        }

        System.out.println("--- Enrolled Courses ---");

        for (String courseName : courses) {
            System.out.println("- " + courseName);
        }

        System.out.println("Total courses: " + courses.size());
    }

    private static void unenrollStudent() throws SQLException {

        int studentId = readPositiveInt("Enter student ID: ");
        int courseId = readPositiveInt("Enter course ID: ");

        System.out.print("Confirm withdrawal? (yes/no): ");
        String answer = sc.nextLine().trim();

        if (answer.equalsIgnoreCase("yes")) {
            boolean deleted = enrollmentService.unenrollStudent(
                    studentId, courseId);

            System.out.println(deleted
                    ? "Student withdrawn successfully."
                    : "Enrollment record not found.");
        } else {
            System.out.println("Withdrawal cancelled.");
        }
    }

    // Input helpers

    private static String readRequired(String prompt) {

        System.out.print(prompt);
        String value = sc.nextLine().trim();

        if (value.isEmpty()) {
            throw new IllegalArgumentException(
                    "This field cannot be empty.");
        }

        return value;
    }

    private static int readInt(String prompt) {

        System.out.print(prompt);

        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Please enter a valid integer.");
        }
    }

    private static int readPositiveInt(String prompt) {

        int value = readInt(prompt);

        if (value <= 0) {
            throw new IllegalArgumentException(
                    "Value must be greater than zero.");
        }

        return value;
    }

    private static BigDecimal readFee(String prompt) {

        System.out.print(prompt);

        try {
            BigDecimal fee =
                    new BigDecimal(sc.nextLine().trim());

            if (fee.signum() < 0) {
                throw new IllegalArgumentException(
                        "Course fee cannot be negative.");
            }

            return fee;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Enter a valid course fee.");
        }
    }
}
