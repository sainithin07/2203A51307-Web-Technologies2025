import java.sql.*;
import java.util.Scanner;

public class CollegeManagement {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/college";
    private static final String USER = "root";
    private static final String PASSWORD = "Sainithin@123";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            createTable(conn);
            
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Choose an operation: \n1. Insert \n2. Update \n3. Delete \n4. Display \n5. Exit");
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        System.out.println("Enter CourseID, Name, Credits:");
                        insertCourse(conn, scanner.nextInt(), scanner.next(), scanner.nextInt());
                        break;
                    case 2:
                        System.out.println("Enter CourseID and new Credits:");
                        updateCourse(conn, scanner.nextInt(), scanner.nextInt());
                        break;
                    case 3:
                        System.out.println("Enter CourseID to delete:");
                        deleteCourse(conn, scanner.nextInt());
                        break;
                    case 4:
                        displayCourses(conn);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Courses (CourseID INT PRIMARY KEY, Name VARCHAR(255), Credits INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table 'Courses' created successfully.");
        }
    }

    private static void insertCourse(Connection conn, int id, String name, int credits) throws SQLException {
        String sql = "INSERT INTO Courses (CourseID, Name, Credits) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, credits);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        }
    }

    private static void updateCourse(Connection conn, int id, int credits) throws SQLException {
        String sql = "UPDATE Courses SET Credits = ? WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, credits);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Record updated successfully.");
            else System.out.println("Course not found.");
        }
    }

    private static void deleteCourse(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM Courses WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("Record deleted successfully.");
            else System.out.println("Course not found.");
        }
    }

    private static void displayCourses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Courses";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("CourseID: " + rs.getInt("CourseID") + " | Name: " + rs.getString("Name") + " | Credits: " + rs.getInt("Credits"));
            }
        }
    }
}
