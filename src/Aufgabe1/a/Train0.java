package Aufgabe1.a;

import java.util.concurrent.Semaphore;

public class Train0 extends TrainDraftForExerciseA{
    public Train0(Semaphore mutexOfSharedTrack, Semaphore availablePassesForThisTrain, Semaphore availablePassesForPartnerTrain) {
        super(0, availablePassesForThisTrain, availablePassesForPartnerTrain, mutexOfSharedTrack);
    }

    @Override
    protected void enterSharedSection() {
        try {
            System.out.println("Lok " + trainId + " prüft, ob sie überhaupt weiterfahren darf.");
            availablePassesForTrain.acquire();
            System.out.println("Lok " + trainId + " fragt kritischen Streckenabschnitt an.");
            mutexOfSharedTrack.acquire();
            System.out.println("Lok " + trainId + " hat kritischen Streckenabschnitt reserviert.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void exitSharedSection() {
        System.out.println("Lok " + trainId + " gibt anderen Zug eine Fahrtberechtigung");
        availablePassesForPartnerTrain.release();
        System.out.println("Lok " + trainId + " hat kritischen Streckenabschnitt freigegeben.");
        mutexOfSharedTrack.release();
    }
}
