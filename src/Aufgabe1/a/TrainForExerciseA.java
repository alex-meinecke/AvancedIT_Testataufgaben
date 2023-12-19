package Aufgabe1.a;

import Aufgabe1.TrainDraftForBothExercises;

import java.util.concurrent.Semaphore;
/*
    TrainForExerciseA konkretisiert alles, was die Züge in Aufgabe a brauchen:
    - Semaphoren, die darstellen, wie viele Durchfahrtsberechtigungen das Lok-Objekt selbst und die andere Lok hat
    - Semaphor, der darstellt, ob der geteilte Bereich gerade benutzt wird
    - Implementierung der enterSharedSection()- and exitSharedSection()-Methoden
 */
public class TrainForExerciseA extends TrainDraftForBothExercises {

    //Durchfahrtsberechtigung für diese Lok
    Semaphore availablePassesForTrain;
    //Durchfahrtsberechtigung für die andere Lok
    Semaphore availablePassesForPartnerTrain;
    //Semaphor, der darstellt, ob der geteilte Bereich gerade benutzt wird
    Semaphore mutexOfSharedTrack;

    //Konstruktor
    public TrainForExerciseA(Integer index, Semaphore mutexOfSharedTrack, Semaphore availablePassesForTrain, Semaphore availablePassesForPartnerTrain) {
        super(index);
        this.mutexOfSharedTrack = mutexOfSharedTrack;
        this.availablePassesForTrain = availablePassesForTrain;
        this.availablePassesForPartnerTrain = availablePassesForPartnerTrain;
    }

    /*
        Funktionsweise der enterSharedSection()-Methode:
        1) Lok prüft erstmal, ob sie überhaupt eine Durchfahrtsberechtigung hat (und wenn nicht, wartet sie ggf.)
        2) Wenn die Durchfahrtsberechtigung vorliegt, fragt die Lok den kritischen Abschnitt an (, wartet ggf.) und reserviert ihn
     */
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

    /*
       Funktionsweise der exitSharedSection()-Methode:
       1) Lok gibt nach erfolgreicher Durchfahrt der anderen Lok eine Zugangsberechtigung
       2) Lok gibt den kritischen Abschnitt für andere Lok wieder frei
       Anmerkung: Reihenfolge ist hier egal
    */
    @Override
    protected void exitSharedSection() {
        System.out.println("Lok " + trainId + " gibt anderen Zug eine Fahrtberechtigung");
        availablePassesForPartnerTrain.release();
        System.out.println("Lok " + trainId + " hat kritischen Streckenabschnitt freigegeben.");
        mutexOfSharedTrack.release();
    }
}
