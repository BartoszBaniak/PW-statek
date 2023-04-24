package com.example.projektpw_statek;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ship {
    private Lock lock = new ReentrantLock();
    private Condition free = lock.newCondition();
    volatile int size;
    int actualCapacity;
    public int counterOnShip = 0;

    public Ship(int size){
        this.size = size;
        this.actualCapacity = 0;
    }
    public void getOnShip(Bridge bridge, Configuration configuration, Circle circle, int ii) throws InterruptedException
    {
        bridge.lock.lock();
        try {
            bridge.counterOnBridge--;
            Path path2=new Path();
            MoveTo moveTo2=new MoveTo();
            moveTo2.setX(configuration.bridgeCircleCoords[ii].x);
            moveTo2.setY(configuration.bridgeCircleCoords[ii].y);
            LineTo lineTo2=new LineTo();
            lineTo2.setX(configuration.shipSquareCoords[counterOnShip].x + configuration.squareSize/2);
            lineTo2.setY(configuration.shipSquareCoords[counterOnShip].y + configuration.squareSize/2);
            path2.getElements().addAll(moveTo2, lineTo2);

            PathTransition pathTransition2=new PathTransition(Duration.millis(225), path2, circle);
            configuration.passengersAnimation.add(pathTransition2);
            pathTransition2.setRate(configuration.AnimationRate);
            pathTransition2.setOnFinished(e->{
                unblock();
                configuration.passengersAnimation.remove(pathTransition2);
            });
            //wykonanie animacji
            Platform.runLater(()->{
                Main.animationPane.getChildren().add(path2);
                pathTransition2.play();
            });

            block();
            Platform.runLater(()->{
                Main.animationPane.getChildren().remove(path2);
            });
            //animacja znikania kola
            counterOnShip++;
            bridge.free.signal();
        } finally {
            bridge.lock.unlock();
        }
    }
    Semaphore bin = new Semaphore(0);
    public void block() throws InterruptedException
    {
        bin.acquire();
    }
    public void unblock()
    {
        bin.release();
    }
}
