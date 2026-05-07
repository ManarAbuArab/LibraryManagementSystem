package dao;

import config.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Integer> getAllbooksids() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT book_id FROM books";

        try {
            Connection conn = DBConnection.getInstance().getConnection();

            try (Statement stat = conn.createStatement();
                 ResultSet rs = stat.executeQuery(sql)) {

                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    ids.add(bookId);
                }
            }

        } catch (SQLException ex) {
                        ex.printStackTrace();

        }

        return ids;
    }
}