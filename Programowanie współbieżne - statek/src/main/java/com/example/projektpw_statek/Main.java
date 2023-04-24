package com.example.projektpw_statek;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static AnchorPane animationPane;
    public static Configuration configuration;
    public static Animation.Status animationStatus = Animation.Status.STOPPED;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        SplitPane root = loader.load();
        animationPane = (AnchorPane) root.getItems().get(1);
        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Statek - symulacja");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}