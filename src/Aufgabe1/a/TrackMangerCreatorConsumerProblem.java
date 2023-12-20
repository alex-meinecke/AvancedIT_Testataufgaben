package Aufgabe1.a;

import Aufgabe1.TrainDraftForBothExercises;

import java.util.concurrent.Semaphore;

public class TrackMangerCreatorConsumerProblem {
    public static void main(String[] args) {
        //Mutex für geteilte Stecke (wird initial nicht benutzt, also permits auf 1)
        Semaphore mutexOfSharedTrack = new Semaphore(1, true);

        //Lok0 soll laut Aufgabenstellung starten, also bekommt sie den ersten Permit und Lok1 bekommt ihn dann später von Lok0 zugeteilt, wenn sie fertig ist
        Semaphore availableInitialPassesForTrain0 = new Semaphore(1, true);
        Semaphore availableInitialPassesForTrain1 = new Semaphore(0,true);

        //Lok 0 und 1 initialisieren und o.g. available passes für jeweils sich selbst und der Partnerlok übergeben
        TrainDraftForBothExercises train0 = new TrainForExerciseA(0,mutexOfSharedTrack,availableInitialPassesForTrain0,availableInitialPassesForTrain1);
        TrainDraftForBothExercises train1 = new TrainForExerciseA(1,mutexOfSharedTrack,availableInitialPassesForTrain1,availableInitialPassesForTrain0);

        /*
            Testfall 1: Loks sind gleich schnell
            train0.setTimeToPassPrivateTrainTrackInSeconds(2);
            train1.setTimeToPassPrivateTrainTrackInSeconds(2);
        */

        /*
            Testfall 2: Lok0 ist schneller mit privater Strecke
            train0.setTimeToPassPrivateTrainTrackInSeconds(2);
            train1.setTimeToPassPrivateTrainTrackInSeconds(10);
        */

        /*
            Testfall 3: Lok1 ist schneller mit privater Strecke
            train0.setTimeToPassPrivateTrainTrackInSeconds(10);
            train1.setTimeToPassPrivateTrainTrackInSeconds(2);
        */
        train0.setTimeToPassPrivateTrainTrackInSeconds(10);
        train1.setTimeToPassPrivateTrainTrackInSeconds(2);

        /*
            Testfall 4 (optional): Geschwindigkeiten und Streckenläge dem Zufall überlassen
        */

        //Threads der Loks starten
        new Thread(train0).start();
        new Thread(train1).start();
    }
}
