package controllers;

import dao.BookDAO;
import dao.BorrowDAO;
import dao.StudentDAO;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Borrow;

public class BorrowController implements Initializable {

    @FXML
    private ComboBox<Integer> booksCombobox;

    @FXML
    private ComboBox<Integer> studentsCombobox;

    @FXML
    private DatePicker borrowDate;

    @FXML
    private DatePicker returnDate;

    @FXML
    private CheckBox status;

    @FXML
    private TableView<Borrow> table;

    @FXML
    private TableColumn<Borrow, Integer> borrowIdTC;

    @FXML
    private TableColumn<Borrow, Integer> bookIdTC;

    @FXML
    private TableColumn<Borrow, Integer> studentIdTC;

    @FXML
    private TableColumn<Borrow, String> borrowDateTC;

    @FXML
    private TableColumn<Borrow, String> returnDateTC;

    @FXML
    private TableColumn<Borrow, Boolean> statusTC;

    private BookDAO bookdao = new BookDAO();
    private StudentDAO studentdao = new StudentDAO();
    private BorrowDAO borrowdao = new BorrowDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borrowIdTC.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        bookIdTC.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        studentIdTC.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        borrowDateTC.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateTC.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusTC.setCellValueFactory(new PropertyValueFactory<>("status"));

        List<Integer> bookIds = bookdao.getAllbooksids();
        booksCombobox.getItems().addAll(bookIds);

        List<Integer> studentIds = studentdao.getAllStudentsids();
        studentsCombobox.getItems().addAll(studentIds);

        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        return;
                    }

                    booksCombobox.setValue(newValue.getBookId());
                    studentsCombobox.setValue(newValue.getStudentId());

                    if (newValue.getBorrowDate() != null && !newValue.getBorrowDate().isEmpty()) {
                        borrowDate.setValue(LocalDate.parse(newValue.getBorrowDate()));
                    } else {
                        borrowDate.setValue(null);
                    }

                    String rd = newValue.getReturnDate();

                    if (rd != null && !rd.isEmpty()) {
                        returnDate.setValue(LocalDate.parse(rd));
                    } else {
                        returnDate.setValue(null);
                    }

                    status.setSelected(newValue.getStatus());
                }
        );
    }

    @FXML
    private void viewHandle(ActionEvent event) {
        List<Borrow> borrows = borrowdao.findAll();
        table.getItems().setAll(borrows);
    }

    @FXML
    private void borrowHandle(ActionEvent event) {
        if (borrowValidator()) {
            Integer bookId = booksCombobox.getSelectionModel().getSelectedItem();
            Integer studentId = studentsCombobox.getSelectionModel().getSelectedItem();
            String bd = borrowDate.getValue().toString();

            Borrow b = new Borrow(studentId, bookId, bd);

            boolean success = borrowdao.insertOne(b);

            if (success) {
                clear();
                viewHandle(event);
                showInfoAlert("Success", "Borrow added successfully.");
            }

        } else {
            showWarningAlert(
                    "Invalid Input",
                    "Missing Data",
                    "Please select book ID, student ID, and borrow date."
            );
        }
    }

    @FXML
    private void returnHandle(ActionEvent event) {
        Borrow b = table.getSelectionModel().getSelectedItem();

        if (b == null) {
            showWarningAlert(
                    "No Selection",
                    "No Record Selected",
                    "Please select a borrow record from the table."
            );
        } else {
            if (returnDate.getValue() == null || !status.isSelected()) {
                showWarningAlert(
                        "Invalid Input",
                        "Missing Data",
                        "Please select return date and check returned status."
                );
            } else {
                b.setReturnDate(returnDate.getValue().toString());
                b.setStatus(status.isSelected());

                boolean success = borrowdao.updateOne(b);

                if (success) {
                    showInfoAlert("Success", "Book returned successfully.");
                    clear();
                    viewHandle(event);
                }
            }
        }
    }

    @FXML
    private void deleteHandle(ActionEvent event) {
        Borrow b = table.getSelectionModel().getSelectedItem();

        if (b == null) {
            showWarningAlert(
                    "No Selection",
                    "No Record Selected",
                    "Please select a borrow record from the table."
            );
        } else {
            boolean confirmed = showConfirmationAlert(
                    "Delete Confirmation",
                    "Are you sure?",
                    "Do you want to delete this borrow record?"
            );

            if (confirmed) {
                boolean success = borrowdao.deleteOne(b);

                if (success) {
                    viewHandle(event);
                    clear();
                    showInfoAlert("Success", "Borrow record deleted successfully.");
                }
            }
        }
    }

    @FXML
    private void borrowedBooksHandle(ActionEvent event) {
        List<Borrow> borrowedBooks = borrowdao.findBorrowedBooks();
        table.getItems().setAll(borrowedBooks);

        if (borrowedBooks.isEmpty()) {
            showInfoAlert("No Data", "There are no borrowed books currently.");
        }
    }

    @FXML
    private void searchbyIds(ActionEvent event) {
        Integer bookId = booksCombobox.getValue();
        Integer studentId = studentsCombobox.getValue();

        if (bookId == null || studentId == null) {
            showWarningAlert(
                    "Invalid Search",
                    "Missing Data",
                    "Please select both book ID and student ID."
            );
            return;
        }

        List<Borrow> results = borrowdao.searchByBookAndStudentId(bookId, studentId);
        table.getItems().setAll(results);

        if (results.isEmpty()) {
            showInfoAlert("No Results", "No borrow records found for the selected IDs.");
        }
    }

    private boolean borrowValidator() {
        return booksCombobox.getValue() != null
                && studentsCombobox.getValue() != null
                && borrowDate.getValue() != null;
    }

    private void clear() {
        booksCombobox.setValue(null);
        studentsCombobox.setValue(null);
        borrowDate.setValue(null);
        returnDate.setValue(null);
        status.setSelected(false);
        table.getSelectionModel().clearSelection();
    }

    private void showWarningAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }
}