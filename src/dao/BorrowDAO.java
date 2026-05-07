package dao;

import config.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import models.Borrow;

public class BorrowDAO {

    public List<Borrow> findAll() {
        List<Borrow> list = new ArrayList<>();
        String sql = "SELECT * FROM borrow";

        try {
            Connection conn = DBConnection.getInstance().getConnection();

            try (Statement stat = conn.createStatement();
                 ResultSet rs = stat.executeQuery(sql)) {

                while (rs.next()) {
                    int borrowId = rs.getInt("borrow_id");
                    int studentId = rs.getInt("student_id");
                    int bookId = rs.getInt("book_id");
                    String borrowDate = rs.getString("borrow_date");
                    String returnDate = rs.getString("return_date");
                    boolean status = rs.getBoolean("status");

                    Borrow b = new Borrow(
                            borrowId,
                            studentId,
                            bookId,
                            borrowDate,
                            returnDate,
                            status
                    );

                    list.add(b);
                }
            }

        } catch (SQLException ex) {
                        ex.printStackTrace();

        }

        return list;
    }

    public boolean insertOne(Borrow b) {
        String sql = "INSERT INTO borrow(student_id, book_id, borrow_date, return_date, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getInstance().getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, b.getStudentId());
                ps.setInt(2, b.getBookId());
                ps.setString(3, b.getBorrowDate());
                ps.setString(4, b.getReturnDate());
                ps.setBoolean(5, b.getStatus());

                int rows = ps.executeUpdate();
                return rows > 0;
            }

        } catch (SQLException ex) {
                        ex.printStackTrace();

        }

        return false;
    }

    public boolean updateOne(Borrow b) {
        String sql = "UPDATE borrow SET return_date = ?, status = ? WHERE borrow_id = ?";

        try {
            Connection conn = DBConnection.getInstance().getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, b.getReturnDate());
                ps.setBoolean(2, b.getStatus());
                ps.setInt(3, b.getBorrowId());

                int rows = ps.executeUpdate();
                return rows > 0;
            }

        } catch (SQLException ex) {
                       ex.printStackTrace();

        }

        return false;
    }

    public boolean deleteOne(Borrow b) {
        String sql = "DELETE FROM borrow WHERE borrow_id = ?";

        try {
            Connection conn = DBConnection.getInstance().getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, b.getBorrowId());

                int rows = ps.executeUpdate();
                return rows > 0;
            }

        } catch (SQLException ex) {
                       ex.printStackTrace();

        }

        return false;
    }

    public List<Borrow> findBorrowedBooks() {
        List<Borrow> list = new ArrayList<>();
        String sql = "SELECT * FROM borrow WHERE status = false";

        try {
            Connection conn = DBConnection.getInstance().getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int borrowId = rs.getInt("borrow_id");
                    int studentId = rs.getInt("student_id");
                    int bookId = rs.getInt("book_id");
                    String borrowDate = rs.getString("borrow_date");
                    String returnDate = rs.getString("return_date");
                    boolean status = rs.getBoolean("status");

                    Borrow b = new Borrow(
                            borrowId,
                            studentId,
                            bookId,
                            borrowDate,
                            returnDate,
                            status
                    );

                    list.add(b);
                }
            }

        } catch (SQLException ex) {
                       ex.printStackTrace();

        }

        return list;
    }

    public List<Borrow> searchByBookAndStudentId(int bookId, int studentId) {
        List<Borrow> list = new ArrayList<>();
        String sql = "SELECT * FROM borrow WHERE book_id = ? AND student_id = ?";

        try {
            Connection conn = DBConnection.getInstance().getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, bookId);
                ps.setInt(2, studentId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int borrowId = rs.getInt("borrow_id");
                        int sId = rs.getInt("student_id");
                        int bId = rs.getInt("book_id");
                        String borrowDate = rs.getString("borrow_date");
                        String returnDate = rs.getString("return_date");
                        boolean status = rs.getBoolean("status");

                        Borrow b = new Borrow(
                                borrowId,
                                sId,
                                bId,
                                borrowDate,
                                returnDate,
                                status
                        );

                        list.add(b);
                    }
                }
            }

        } catch (SQLException ex) {
                        ex.printStackTrace();

        }

        return list;
    }
}