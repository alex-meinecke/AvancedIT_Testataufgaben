package Aufgabe1.b;

import java.util.concurrent.Semaphore;

public class TrackManager {
    public static void main(String[] args) {
        Boolean isSharedAreaBusy = false;
        Semaphore mutexAccessOfSharedArea = new Semaphore(1, true);
        Train[] trains = new Train[2];

        for (int i = 0; i < trains.length; i++) {
            trains[i] = new Train(i, mutexAccessOfSharedArea, isSharedAreaBusy, trains);
        }

        trains[0].setTimeToPassPrivateTrainTrackInSeconds(4);
        trains[1].setTimeToPassPrivateTrainTrackInSeconds(2);

        for (Train currentTrainToStart: trains) {
            new Thread(currentTrainToStart).start();
        }

    }
}
