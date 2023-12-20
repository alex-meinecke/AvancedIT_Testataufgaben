package Aufgabe1.b;

import java.util.concurrent.Semaphore;

public class TrackManagerForPrivateSemaphoreSolutionOld {
    public static void main(String[] args) {
        Boolean isSharedAreaBusy = false;
        Semaphore mutexAccessOfSharedArea = new Semaphore(1, true);
        TrainOld[] trains = new TrainOld[2];


        for (int i = 0; i < trains.length; i++) {
            trains[i] = new TrainOld(i, mutexAccessOfSharedArea, isSharedAreaBusy, trains);
        }

        trains[0].setTimeToPassPrivateTrainTrackInSeconds(4);
        trains[1].setTimeToPassPrivateTrainTrackInSeconds(2);

        for (TrainOld currentTrainToStart: trains) {
            new Thread(currentTrainToStart).start();
        }

    }
}
