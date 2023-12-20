package Aufgabe1.b;

import java.util.concurrent.Semaphore;

public class TrackManagerForPrivateSemaphoreSolution {

    public static void main(String[] args) {
        Boolean[] isWaiting = new Boolean[2];
        Semaphore[] privateSemaphoresForTrains = new Semaphore[2];
        TrainDraftForExerciseB[] trains = new TrainDraftForExerciseB[2];
        Semaphore mutexForSharedData = new Semaphore(1,true);
        Boolean isSharedTrackAreaBusy = false;

        privateSemaphoresForTrains[0] = new Semaphore(0,true);
        privateSemaphoresForTrains[1] = new Semaphore(0,true);

        isWaiting[0]=false;
        isWaiting[1]=false;

        trains[0] = new Train0(trains, isWaiting, privateSemaphoresForTrains, mutexForSharedData, isSharedTrackAreaBusy);
        trains[1] = new Train1(trains, isWaiting, privateSemaphoresForTrains, mutexForSharedData, isSharedTrackAreaBusy);

        /*
            Testfall 1: Loks sind gleich schnell
            trains[0].setTimeToPassPrivateTrainTrackInSeconds(2);
            trains[1].setTimeToPassPrivateTrainTrackInSeconds(2);
        */

        /*
            Testfall 2: Lok0 ist schneller mit privater Strecke
            trains[0].setTimeToPassPrivateTrainTrackInSeconds(2);
            trains[1].setTimeToPassPrivateTrainTrackInSeconds(10);
        */

        /*
            Testfall 3: Lok1 ist schneller mit privater Strecke
            trains[0].setTimeToPassPrivateTrainTrackInSeconds(10);
            trains[1].setTimeToPassPrivateTrainTrackInSeconds(2);
        */

        trains[0].setTimeToPassPrivateTrainTrackInSeconds(10);
        trains[1].setTimeToPassPrivateTrainTrackInSeconds(2);

        new Thread(trains[0]).start();
        new Thread(trains[1]).start();
    }

}
