package Aufgabe1.b;

import Aufgabe1.TrainDraftForBothExercises;

import java.util.concurrent.Semaphore;


public abstract class TrainDraftForExerciseB extends TrainDraftForBothExercises {
    public Semaphore privateSemaphore = new Semaphore(0, true);
    Semaphore mutexAccessOfSharedArea;
    private Boolean isWaitingToEnterSharedArea = false;
    Boolean isSharedAreaBusy;
    Train[] trains;
    Integer partnerTrainID;

    private Integer numberOfSuccessfulRuns = 0;

    public TrainDraftForExerciseB(Integer trainId) {
        super(trainId);
    }

}
