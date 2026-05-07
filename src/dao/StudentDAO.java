package dao;

import config.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public List<Integer> getAllStudentsids() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT student_id FROM students";

        try {
            Connection conn = DBConnection.getInstance().getConnection();

            try (Statement stat = conn.createStatement();
                 ResultSet rs = stat.executeQuery(sql)) {

                while (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    ids.add(studentId);
                }
            }

        } catch (SQLException ex) {
                      ex.printStackTrace();

        }

        return ids;
    }
}
