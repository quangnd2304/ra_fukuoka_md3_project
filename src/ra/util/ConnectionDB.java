package ra.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/ShopManagement";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "1234$";

    //Phương thức tạo đối tượng Connection để làm việc với db
    public static Connection openConnection() {
        Connection conn = null;
        try {
            //Set driver de ket noi
            Class.forName(DRIVER);
            //Khởi tạo đối tuợng connection từ Driver Manager
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    //Phương thức đóng connection và callableStatement sau khi làm việc xong
    public static void closeConnection(Connection conn, CallableStatement callSt) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (callSt != null) {
            try {
                callSt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
