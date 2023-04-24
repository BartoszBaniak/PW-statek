package com.example.projektpw_statek;

import javafx.animation.Animation;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Configuration {
    int passengersNum;
    int shipSize;
    int bridgeSize;
    int licznikPrzeplyw = 0;
    public Configuration (int screenWidth, int screenHeight, int PassengersNum, int ShipSize, int BridgeSize){
        this.shipSize = ShipSize;
        this.bridgeSize = BridgeSize;
        this.passengersNum = PassengersNum;
        //this.LAND_SIZE = landSize;
        this.landSquareSize = 80;
        this.squareSize = 50;
        this.circleRadius = 20;
        this.squareSpace = 10;
        int free_space = screenWidth - shipSize * (this.squareSize + this.squareSpace) + this.squareSpace;

        //Rozmieszczenie kwadratu lądu i położeń początkowych kół
        this.landSquareCoords = new XYCoord[1];
        this.landCircleCoords = new XYCoord[1];
        for(int i = 0; i < this.landSquareCoords.length; ++i) {
            this.landSquareCoords[i] = new XYCoord();
            this.landSquareCoords[i].x = 40;
            this.landSquareCoords[i].y = 180;
            this.landCircleCoords[i] = new XYCoord();
            this.landCircleCoords[i].x = this.landSquareCoords[i].x + this.landSquareSize / 2;
            this.landCircleCoords[i].y = this.landSquareCoords[i].y + this.landSquareSize / 2;
        }

        //Rozmieszczenie kwadratów mostka i położeń początkowych kół
        this.bridgeSquareCoords = new XYCoord[bridgeSize];
        this.bridgeCircleCoords = new XYCoord[bridgeSize];
        for(int i = 0; i < bridgeSquareCoords.length; ++i) {
            this.bridgeSquareCoords[i] = new XYCoord();
            this.bridgeSquareCoords[i].y = 300 - i * (this.squareSize + this.squareSpace);
            this.bridgeSquareCoords[i].x = 350;
            this.bridgeCircleCoords[i] = new XYCoord();
            this.bridgeCircleCoords[i].x = this.bridgeSquareCoords[i].x + this.squareSize / 2;
            this.bridgeCircleCoords[i].y = this.bridgeSquareCoords[i].y + this.squareSize / 2;
        }

        //Rozmieszczenie kwadratów statku i położeń początkowych kół
        shipSquareCoords = new XYCoord[shipSize];
        shipCircleCoords = new XYCoord[shipSize];
        for(int i = 0; i < shipSquareCoords.length; ++i) {
            this.shipSquareCoords[i] = new XYCoord();
            this.shipSquareCoords[i].x = 620;
            this.shipSquareCoords[i].y = 40 + i * (this.squareSize + this.squareSpace);
            this.shipCircleCoords[i] = new XYCoord();
            this.shipCircleCoords[i].x = this.shipSquareCoords[i].x + this.squareSize / 2;
            this.shipCircleCoords[i].y = this.shipSquareCoords[i].y + this.squareSize / 2;
        }

    }

    /***
     * Tablica współrzędnych położenia kwadratów mostku
     */
    public XYCoord[] bridgeSquareCoords;
    /***
     * Tablica współrzędnych położenia kwadratów statku
     */
    public XYCoord[] shipSquareCoords;
    /***
     * Tablica współrzędnych położenia kwadratów lądu
     */
    public XYCoord[] landSquareCoords;
    /***
     * Tablica współrzędnych środków kółek mostku
     */
    public XYCoord[] bridgeCircleCoords;
    /***
     * Tablica wpółrzędnych środków kółek statku
     */
    public XYCoord[] shipCircleCoords;
    /***
     * Tablica współrzędnych środków kółek lądu
     */
    public XYCoord[] landCircleCoords;
    public int landSquareSize;
    public int squareSize;
    public int squareSpace;
    public int circleRadius;

    public static long SHIP_INTERVAL;
    public static int PASSENGERS_COUNT;
    public static int MIN_TIME_LIMIT;
    public static int MAX_TIME_LIMIT;
    public static int SHIP_SIZE;
    public static int BRIDGE_SIZE;
    double AnimationRate;

    Rectangle[] landSquares;
    Rectangle[] bridgeSquares;
    Rectangle[] shipSquares;

    List<Animation> passengersAnimation;

    public void prepareAnimation() {
        this.passengersAnimation = new ArrayList();
        this.AnimationRate = 1;
        this.landSquares = new Rectangle[1];

        int i;
        for(i = 0; i < landSquareCoords.length; ++i) {
            this.landSquares[i] = new Rectangle(this.landSquareCoords[i].x, this.landSquareCoords[i].y, this.landSquareSize, this.landSquareSize);
            this.landSquares[i].setFill(Color.LIGHTGRAY);
            this.landSquares[i].setStroke(Color.BLACK);
            Main.animationPane.getChildren().addAll(new Node[]{this.landSquares[i]});
        }

        this.bridgeSquares = new Rectangle[this.bridgeSize];
        for(i = 0; i < bridgeSquareCoords.length; ++i) {
            this.bridgeSquares[i] = new Rectangle(this.bridgeSquareCoords[i].x, this.bridgeSquareCoords[i].y, this.squareSize, this.squareSize);
            this.bridgeSquares[i].setFill(Color.SANDYBROWN);
            this.bridgeSquares[i].setStroke(Color.BLACK);
            Main.animationPane.getChildren().addAll(new Node[]{this.bridgeSquares[i]});
        }

        this.shipSquares = new Rectangle[this.shipSize];
        for(i = 0; i < shipSquareCoords.length; ++i) {
            this.shipSquares[i] = new Rectangle(this.shipSquareCoords[i].x, this.shipSquareCoords[i].y, this.squareSize, this.squareSize);
            this.shipSquares[i].setFill(Color.LIGHTBLUE);
            this.shipSquares[i].setStroke(Color.BLACK);
            Main.animationPane.getChildren().addAll(new Node[]{this.shipSquares[i]});
        }
    }

    public void pauseAnimation() {
        synchronized (passengersAnimation) {
            for (Animation a : passengersAnimation) {
                a.pause();
            }
        }
    }

    public void stopAnimation() {
        for (Animation a : passengersAnimation) {
            a.stop();
        }
    }

    public void resumeAnimation() {
        synchronized (passengersAnimation) {
            for (Animation a : passengersAnimation) {
                a.play();
            }
        }
    }

    Passenger[] passengersList = new Passenger[passengersNum];
    Thread[] passengersList2;

    public void startThreads() {

//        System.out.println(passengersNum);
//        System.out.println(bridgeSize);
//        System.out.println(shipSize);
//        System.out.println(MIN_TIME_LIMIT);
//        System.out.println(MAX_TIME_LIMIT);
//        System.out.println(passengersList.length);
        Bridge bridge = new Bridge(bridgeSize, shipSize);
        bridge.i = 0;
        Ship ship = new Ship(shipSize);
        Capitan capitan = new Capitan(bridge,ship, this);

        Runnable travelInterval = capitan::shipTravel;

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(travelInterval, SHIP_INTERVAL + 3, SHIP_INTERVAL + 3, TimeUnit.SECONDS);

        //Tworzymy pasazerow

        Passenger.j = 0;
        passengersList2 = new Thread[passengersNum];
        for(int i = 0; i < passengersList2.length; i++){
            passengersList2[i] = new Thread(new Passenger("P-"+i,bridge,ship, MIN_TIME_LIMIT, MAX_TIME_LIMIT, this, i), "P-"+i);
        }

        //Uruchamiamy wątki
        for (int i = 0; i < passengersList2.length; i++){
            passengersList2[i].start();
        }
    }

    public void interruptThreads() {
        for(int i = 0; i < passengersList2.length; i++) {
            passengersList2[i].interrupt();
        }
    }
}
