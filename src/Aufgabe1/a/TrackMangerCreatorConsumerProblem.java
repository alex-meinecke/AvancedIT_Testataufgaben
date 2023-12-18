package Aufgabe1.a;

import java.util.concurrent.Semaphore;

public class TrackMangerCreatorConsumerProblem {
    public static void main(String[] args) {
        Semaphore aviablePasses = new Semaphore(0, true);
        Semaphore mutexOfSharedTrack = new Semaphore(1,true);

        Train train0 = new Train(0,aviablePasses,mutexOfSharedTrack);
        Train train1 = new Train(1,aviablePasses,mutexOfSharedTrack);

        train0.setTimeToPassPrivateTrainTrackInSeconds(4);
        train1.setTimeToPassPrivateTrainTrackInSeconds(2);

        new Thread(train0).start();
        new Thread(train1).start();
    }
}
