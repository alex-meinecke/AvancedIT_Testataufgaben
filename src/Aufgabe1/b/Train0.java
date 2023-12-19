package Aufgabe1.b;

import java.util.concurrent.Semaphore;

public class Train0 extends TrainDraftForExerciseB{

    public Train0(Semaphore mutexAccessOfSharedArea, Boolean isSharedAreaBusy, Train[] trains) {
        super(0);
        this.mutexAccessOfSharedArea = mutexAccessOfSharedArea;
        this.isSharedAreaBusy = isSharedAreaBusy;
        this.partnerTrainID = (trainId + 1) % 2;
        this.trains = trains;
    }

    @Override
    protected void enterSharedSection() {
        try {
            mutexAccessOfSharedArea.acquire();
            if (!isSharedAreaBusy){
                isSharedAreaBusy = true;
                privateSemaphore.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void exitSharedSection() {

    }
}
