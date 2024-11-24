package com.johnverz.fxapp;

import com.johnverz.fxapp.utility.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 350);
        stage.setTitle("App Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        // (new Database()).generateUser("johnverz", "secret");

        launch();
    }
}