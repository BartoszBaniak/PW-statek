package com.example.projektpw_statek;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class Bridge {
    volatile int size;
    public Lock lock = new ReentrantLock();
    public Condition free = lock.newCondition();
    Configuration configuration;

    public int maxShip;
    public int counterWaitingPassenger = 0;
    public int counterOnBridge = 0;
    int i;
    int licznik = 0;

    public void shipSail() throws InterruptedException
    {
        lock.lock();
        try
        {
            free.signalAll();
        }
        finally {
            lock.unlock();
        }
    }


    public Bridge(int size, int liczbaNaStatek){
        this.size = size;
        this.maxShip = liczbaNaStatek;

    }
    public int getOnBridge(Ship ship, Configuration configuration, Circle circle, int id, int j) throws InterruptedException {
        lock.lock();
        counterWaitingPassenger++;
        int ii;
        try
        {
            while(counterOnBridge >= size || (maxShip - ship.counterOnShip - counterOnBridge) == 0){
                free.await();
            }
            counterWaitingPassenger--;
            ii = i;
            i = (i+1) % configuration.bridgeSize;
            Path path=new Path();
            MoveTo moveTo=new MoveTo();
            moveTo.setX(configuration.landCircleCoords[0].x);
            moveTo.setY(configuration.landCircleCoords[0].y);
            LineTo lineTo = new LineTo();
            lineTo.setX(configuration.bridgeCircleCoords[ii].x);
            lineTo.setY(configuration.bridgeCircleCoords[ii].y);
            //System.out.println(ii);
            path.getElements().addAll(moveTo, lineTo);
            PathTransition pathTransition =new PathTransition(Duration.millis(225), path, circle);
            pathTransition.setRate(configuration.AnimationRate);
            configuration.passengersAnimation.add(pathTransition);
            pathTransition.setOnFinished(e->{
                unblock();
                configuration.passengersAnimation.remove(pathTransition);
            });
            //wykoananie animacji
            Platform.runLater(()->{
                Main.animationPane.getChildren().add(path);
                pathTransition.play();
            });
            block();
            Platform.runLater(()->{
                Main.animationPane.getChildren().remove(path);
            });
            counterOnBridge++;
        }
        finally {
            lock.unlock();
        }

        return ii;
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
