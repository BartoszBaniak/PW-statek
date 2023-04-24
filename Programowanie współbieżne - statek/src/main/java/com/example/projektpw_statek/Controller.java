package com.example.projektpw_statek;

import javafx.animation.Animation.Status;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
public class Controller {
    @FXML
    private TextField PASSENGERS_COUNT_TEXT;
    @FXML
    private TextField BRIDGE_SIZE_TEXT;
    @FXML
    private TextField SHIP_SIZE_TEXT;

    @FXML
    private TextField LAND_SIZE_TEXT;
    @FXML
    private TextField MIN_TIME_LIMIT_TEXT;
    @FXML
    private TextField MAX_TIME_LIMIT_TEXT;
    @FXML
    private TextField SHIP_INTERVAL_TEXT;
    @FXML
    private Button saveButton;
    @FXML
    private Button runButton;
    @FXML
    private Button pauseResumeButton;
    @FXML
    private Button stopButton;


    @FXML
    public void initialize() {
        runButton.setDisable(true);
        //pauseResumeButton.setDisable(true);
        //pauseResumeButton.setText("Pause/Resume");
        stopButton.setDisable(true);
    }

    @FXML
    protected void onRunButtonAction() {
        if (Main.configuration != null) {
            Main.animationStatus = Status.RUNNING;
            Main.configuration.startThreads();
            runButton.setDisable(true);
            //pauseResumeButton.setDisable(false);
            //pauseResumeButton.setText("Pause");
            stopButton.setDisable(false);
        }
    }

//    @FXML
//    protected void onPauseResumeButtonAction() {
//        if (Main.configuration != null) {
//            if (Main.animationStatus == Status.PAUSED) {
//                Main.animationStatus = Status.RUNNING;
//                Main.configuration.resumeAnimation();
//                pauseResumeButton.setText("Pause");
//                runButton.setDisable(true);
//                stopButton.setDisable(false);
//            } else if (Main.animationStatus == Status.RUNNING) {
//                Main.animationStatus = Status.PAUSED;
//                Main.configuration.pauseAnimation();
//                saveButton.setDisable(true);
//                runButton.setDisable(true);
//                pauseResumeButton.setText("Resume");
//                stopButton.setDisable(true);
//            }
//        }
//    }

    @FXML
    protected void onStopButtonAction() {
        if (Main.animationStatus == Status.RUNNING) {
            Main.animationStatus = Status.STOPPED;
            Main.configuration.interruptThreads();
            Main.animationPane.getChildren().clear();
            saveButton.setDisable(false);
            runButton.setDisable(true);
            pauseResumeButton.setDisable(true);
            pauseResumeButton.setText("Pause/Resume");
            stopButton.setDisable(true);
        }
    }

    @FXML
    protected void onSaveButtonAction() {
        try {
            int screenWidth = (int) Main.animationPane.getWidth();
            int screenHeight = (int) Main.animationPane.getHeight();
            int passengersNum = 0;
            int shipSize = 0;
            int bridgeSize = 0;
            try {
                passengersNum = Integer.parseInt(PASSENGERS_COUNT_TEXT.getText());
            } catch (NumberFormatException e) {
                PASSENGERS_COUNT_TEXT.setText("Error");
                throw new DataErrorException();
            }
            try {
                bridgeSize = Integer.parseInt(BRIDGE_SIZE_TEXT.getText());
            } catch (NumberFormatException e) {
                BRIDGE_SIZE_TEXT.setText("Error");
                throw new DataErrorException();
            }
            try {
                shipSize = Integer.parseInt(SHIP_SIZE_TEXT.getText());
            } catch (NumberFormatException e) {
                SHIP_SIZE_TEXT.setText("Error");
                throw new DataErrorException();
            }
            Main.configuration = new Configuration(screenWidth, screenHeight, passengersNum, shipSize, bridgeSize);
            try {
                Main.configuration.MIN_TIME_LIMIT = Integer.parseInt(MIN_TIME_LIMIT_TEXT.getText());
            } catch (NumberFormatException e) {
                MIN_TIME_LIMIT_TEXT.setText("Error");
                throw new DataErrorException();
            }
            try {
                Main.configuration.MAX_TIME_LIMIT = Integer.parseInt(MAX_TIME_LIMIT_TEXT.getText());
            } catch (NumberFormatException e) {
                MAX_TIME_LIMIT_TEXT.setText("Error");
                throw new DataErrorException();
            }

            try {
                Main.configuration.SHIP_INTERVAL = Integer.parseInt(SHIP_INTERVAL_TEXT.getText());
            } catch (NumberFormatException e) {
                SHIP_INTERVAL_TEXT.setText("Error");
                throw new DataErrorException();
            }

            Main.configuration.prepareAnimation();
            saveButton.setDisable(true);
            runButton.setDisable(false);

        } catch (DataErrorException e) {

        }
    }
}