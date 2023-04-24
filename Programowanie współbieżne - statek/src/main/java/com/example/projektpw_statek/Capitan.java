package com.example.projektpw_statek;

public class Capitan {
    volatile Bridge bridge;
    volatile Ship ship;

    Configuration configuration;
    public Capitan(Bridge bridge,Ship ship, Configuration configuration){
        this.bridge = bridge;
        this.ship = ship;
        this.configuration = configuration;
    }

    public void shipTravel() {
        ship.counterOnShip = 0;
        System.out.println("Kapitan sprawdza czy mostek jest pusty i statek wyrusza w rejs!\n");
        try {
            bridge.shipSail();
            configuration.licznikPrzeplyw++;

        } catch (InterruptedException ignored) {

        }
    }
}
