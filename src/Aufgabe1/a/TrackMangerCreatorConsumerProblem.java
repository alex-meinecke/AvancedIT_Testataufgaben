package Aufgabe1.a;

import Aufgabe1.TrainDraftForBothExercises;

import java.util.concurrent.Semaphore;

public class TrackMangerCreatorConsumerProblem {
    public static void main(String[] args) {
        Semaphore mutexOfSharedTrack = new Semaphore(1, true);
        Semaphore aviableIntialPassesForTrain0 = new Semaphore(1, true);
        Semaphore aviableIntialPassesForTrain1 = new Semaphore(0,true);

        TrainDraftForBothExercises train0 = new Train0(mutexOfSharedTrack,aviableIntialPassesForTrain0,aviableIntialPassesForTrain1);
        TrainDraftForBothExercises train1 = new Train1(mutexOfSharedTrack,aviableIntialPassesForTrain1,aviableIntialPassesForTrain0);

        train0.setTimeToPassPrivateTrainTrackInSeconds(2);
        train1.setTimeToPassPrivateTrainTrackInSeconds(20);

        new Thread(train0).start();
        new Thread(train1).start();
    }
}
