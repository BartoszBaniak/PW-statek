package com.example.projektpw_statek;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Passenger extends Thread {
    Random randomTime = new Random();
    int minTimeLimit, maxTimeLimit;
    volatile Bridge bridge;
    volatile Ship ship;
    int id;
    static int j;
    Configuration configuration;
    static Color[] kolory = new Color[]{Color.RED, Color.SILVER, Color.GRAY, Color.ORANGE, Color.LAVENDER};


    public Passenger(String name, Bridge bridge, Ship ship, int minTimeLimit, int maxTimeLimit, Configuration configuration, int id) {
        super(name);
        this.bridge = bridge;
        this.minTimeLimit = minTimeLimit;
        this.maxTimeLimit = maxTimeLimit;
        this.ship = ship;
        this.configuration = configuration;
        this.id = id;
    }

    public void run() {

        //Thread.sleep(randomTime.nextInt(maxTimeLimit - minTimeLimit));
        try {
            //final int ii = i + 1;
            //Uwtworzenie kola
            Circle circle = new Circle();
            //przypisanie miejsca pojawienia
            circle.setCenterX(configuration.landCircleCoords[0].x);
            circle.setCenterY(configuration.landCircleCoords[0].y);
            circle.setRadius(10.0);
            //przypisanie koloru
            int whichColor = id % kolory.length;
            circle.setFill(kolory[whichColor]);
            circle.setStrokeWidth(20);
            //utworzenie animacji pojawienie na ekranie
            ScaleTransition scaleTransition = new ScaleTransition();
            //przypisanie czasu produkcji
            int dt = 200;
            scaleTransition.setDuration(Duration.millis(dt));
            //przypisanie animacji do kola
            scaleTransition.setNode(circle);
            scaleTransition.setByY(1.5);
            scaleTransition.setByX(1.5);
            scaleTransition.setAutoReverse(false);
            scaleTransition.setRate(configuration.AnimationRate);
            configuration.passengersAnimation.add(scaleTransition);
            scaleTransition.setOnFinished(e -> {
                unblock();
            });
            //wykoananie produkcji
            Platform.runLater(() -> {
                scaleTransition.play();
            });
            block();
            Platform.runLater(() -> {
                Main.animationPane.getChildren().add(circle);
            });

            //Sprawdzam czy jest wolny mostek
            int a;
            a = bridge.getOnBridge(ship, configuration, circle, id, j);

            //wchodzi na mostek
            System.out.println("Wchodzi na mostek -> [" + getName() + "], " + bridge.counterOnBridge);
            //Czas przejscia przez mostek
            Thread.sleep(randomTime.nextInt(maxTimeLimit - minTimeLimit));

            //Wchodzi na statek
            ship.getOnShip(bridge, configuration, circle, a);
            System.out.println("Wchodzi na statek -> [" + getName() + "], " + ship.counterOnShip);
            ScaleTransition scaleTransition2=new ScaleTransition();
            scaleTransition2.setDuration(Duration.millis(configuration.SHIP_INTERVAL*1000-100));
            //przypisanie animacji do kola
            scaleTransition2.setNode(circle);
            scaleTransition2.setByY(-1.5);
            scaleTransition2.setByX(-1.5);
            scaleTransition2.setCycleCount(1);
            scaleTransition2.setAutoReverse(false);
            configuration.passengersAnimation.add(scaleTransition2);
            scaleTransition2.setRate(configuration.AnimationRate);
            // Rejestracja reakcji na zakonczenie animacji.
            scaleTransition2.setOnFinished(e -> {
                unblock();
                configuration.passengersAnimation.remove(scaleTransition2);
            });
            //wykonanie animacji
            Platform.runLater(() -> {
                scaleTransition2.play();
            });
            block();
            //usuniecie kola z ekranu
            Platform.runLater(() -> {
                Main.animationPane.getChildren().remove(circle);
            });

                } catch (InterruptedException e) {

          }


    }
        Semaphore bin=new Semaphore(0);
        public void block() throws InterruptedException
        {
            bin.acquire();
        }
        public void unblock()
        {
            bin.release();
        }
}