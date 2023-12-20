package Aufgabe1.b;

import java.util.concurrent.Semaphore;

public class TrainOld extends Aufgabe1.TrainDraftForBothExercises {

    public Semaphore privateSemaphore = new Semaphore(0, true);
    private Semaphore mutexAccessOfSharedArea;
    private Boolean isWaitingToEnterSharedArea = false;
    private Boolean isSharedAreaBusy;
    TrainOld[] trains;
    private Integer partnerTrainID;

    private Integer numberOfSuccessfulRuns = 0;

    public TrainOld(Integer trainId, Semaphore mutexAccessOfSharedArea, Boolean isSharedAreaBusy, TrainOld[] trains) {
        super(trainId);

        this.mutexAccessOfSharedArea = mutexAccessOfSharedArea;
        this.isSharedAreaBusy = isSharedAreaBusy;
        this.partnerTrainID = (trainId + 1) % 2;
        this.trains = trains;
    }

    @Override
    protected void enterSharedSection() {
        try {
            mutexAccessOfSharedArea.acquire();
            if (!isSharedAreaBusy && ((trainId == 0) || (numberOfSuccessfulRuns < trains[partnerTrainID].numberOfSuccessfulRuns && trainId == 1))){
                isSharedAreaBusy = true;
                privateSemaphore.release();
            } if (trains[0].isWaitingToEnterSharedArea && trains[1].isWaitingToEnterSharedArea) {
                isSharedAreaBusy = false;

            } else {
                isWaitingToEnterSharedArea = true;
                isSharedAreaBusy=false;

                System.out.println("Lok" + trainId + " wartet");
            }
            mutexAccessOfSharedArea.release();

            privateSemaphore.acquire();
            numberOfSuccessfulRuns++;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void exitSharedSection() {
        try {
            mutexAccessOfSharedArea.acquire();

            if (trains[partnerTrainID].isWaitingToEnterSharedArea) {
                trains[partnerTrainID].isWaitingToEnterSharedArea = false;
                System.out.println("Partnerzug gibt Zug frei :" + partnerTrainID);
                trains[partnerTrainID].privateSemaphore.release();
            } else {
                isSharedAreaBusy = false;
            }

            mutexAccessOfSharedArea.release();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}