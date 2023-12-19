package Aufgabe1.a;

import Aufgabe1.TrainDraftForBothExercises;

import java.util.concurrent.Semaphore;
/*
    TrainDraftForExerciseA definiert generelles, was alle Züge in Aufgabe a brauchen:
    - der Semaphor availablePassesForTrain1, der angibt
      wie viele Fahrten der Lok1 zur Verfügung stehen

 */
public abstract class TrainDraftForExerciseA extends TrainDraftForBothExercises {

    Semaphore availablePassesForTrain;
    Semaphore availablePassesForPartnerTrain;
    Semaphore mutexOfSharedTrack;

    public TrainDraftForExerciseA(Integer trainId, Semaphore aviablePassesForThisTrain, Semaphore availablePassesForPartnerTrain, Semaphore mutexOfSharedTrack) {
        super(trainId);
        this.availablePassesForTrain = aviablePassesForThisTrain;
        this.availablePassesForPartnerTrain = availablePassesForPartnerTrain;
        this.mutexOfSharedTrack = mutexOfSharedTrack;
    }


}
