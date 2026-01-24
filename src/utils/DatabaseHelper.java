package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class DatabaseHelper {
    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Properties prop = new Properties();
                FileInputStream fis = new FileInputStream("config.properties");
                prop.load(fis);

                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.user");
                String pass = prop.getProperty("db.password");

                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}