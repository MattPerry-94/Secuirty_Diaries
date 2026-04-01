package org.bdd_manager;

import java.sql.*;
import java.time.LocalDateTime;

public class DiaryManager {
    public static boolean addEntry(String name, String type,
                                   String content, LocalDateTime begin,
                                   LocalDateTime end){
        Connection connection = DatabaseConnection.getConnection();
        String sql_request = "INSERT INTO diaries (title, type, content, created_date, finished_date) VALUES(?,?,?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql_request);
            pstmt.setString(1,name);
            pstmt.setString(2,type);
            pstmt.setString(3,content);
            pstmt.setTimestamp(4, Timestamp.valueOf(begin));
            pstmt.setTimestamp(5, Timestamp.valueOf(end));
           return pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getEntriesOfSevenDays(){
        Connection connection = DatabaseConnection.getConnection();
        String sql_request = "SELECT * FROM diaries WHERE created_date >= now() - INTERVAL 7 DAY ORDER BY created_date DESC";

        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql_request);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
