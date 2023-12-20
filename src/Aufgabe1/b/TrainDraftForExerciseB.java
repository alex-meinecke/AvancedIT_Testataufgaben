package Aufgabe1.b;

import Aufgabe1.TrainDraftForBothExercises;

import java.util.concurrent.Semaphore;

/*
    TrainDraftForExerciseB definiert generelles, was alle Züge in Aufgabe b brauchen:
    - ein Array der Loks
    - ein Array über den Wartestatus der Loks
    - ein Array der privaten Semaphoren
    - ein mutex-Semaphor für die geteilten Daten
    - ein Boolean, der anzeigt, ob der geteilte Bereich gerade benutzt wird
    - die Definition der Nummer von Lok 0 und 1
    - ein Counter, mit dem die bereits erfolgtem Durchfahrten einer Lok dokumentiert werden können
 */
public abstract class TrainDraftForExerciseB extends TrainDraftForBothExercises {

    //ein Array der Loks
    TrainDraftForExerciseB[] trains;
    //ein Array über den Wartestatus der Loks
    Boolean[] isWaiting;
    //ein Array der privaten Semaphoren
    Semaphore[] privateSemaphoresForTrains;
    //ein mutex-Semaphor für die geteilten Daten
    Semaphore mutexForSharedData;
    //ein Boolean, der anzeigt, ob der geteilte Bereich gerade benutzt wird
    Boolean isSharedTrackAreaBusy;

    //die Definition der Nummer von Lok 0 und 1
    static final Integer train0Id = 0;
    static final Integer train1Id = 1;
    //ein Counter, mit dem die bereits erfolgtem Durchfahrten einer Lok dokumentiert werden können
    Integer numberOfDrivesThroughTheSharedArea = 0;

    // Konstruktor
    public TrainDraftForExerciseB(Integer trainId, TrainDraftForExerciseB[] trains, Boolean[] isWaiting, Semaphore[] privateSemaphoresForTrains, Semaphore mutexForSharedData, Boolean isSharedTrackAreaBusy) {
        super(trainId);
        this.trains = trains;
        this.isWaiting = isWaiting;
        this.privateSemaphoresForTrains = privateSemaphoresForTrains;
        this.mutexForSharedData = mutexForSharedData;
        this.isSharedTrackAreaBusy = isSharedTrackAreaBusy;
    }
}
