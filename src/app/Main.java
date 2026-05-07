package app;

import config.DBConnection;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        URL fxmlUrl = getClass().getResource("/views/Borrow.fxml");

        if (fxmlUrl == null) {
            fxmlUrl = new File("src/views/Borrow.fxml").toURI().toURL();
        }

        Parent root = FXMLLoader.load(fxmlUrl);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Library Management System");
        stage.show();
    }

    @Override
    public void stop() {
        try {
            DBConnection.getInstance().closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}