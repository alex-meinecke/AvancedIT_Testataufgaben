package Aufgabe1.a;

import java.util.concurrent.Semaphore;

public class Train extends Aufgabe1.Train {
    private Semaphore aviablePassesForTrain1;
    private Semaphore mutexOfSharedTrack;

    public Train(Integer trainId, Semaphore aviablePassesForTrain1, Semaphore mutexOfSharedTrack) {
        super(trainId);
        this.aviablePassesForTrain1 = aviablePassesForTrain1;
        this.mutexOfSharedTrack = mutexOfSharedTrack;
    }

    @Override
    protected void enterSharedSection() {
        if (trainId==0){
            try {
                System.out.println("Lok " + trainId + " fragt kritischen Streckenabschnitt an.");
                mutexOfSharedTrack.acquire();
                System.out.println("Lok " + trainId + " hat kritischen Streckenabschnitt reserviert.");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        } else if (trainId==1){
            try {
                System.out.println("Lok " + trainId + " prüft, ob sie überhaupt weiterfahren darf.");
                aviablePassesForTrain1.acquire();

                System.out.println("Lok " + trainId + " fragt kritischen Streckenabschnitt an.");
                mutexOfSharedTrack.acquire();
                System.out.println("Lok " + trainId + " hat kritischen Streckenabschnitt reserviert.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @Override
    protected void exitSharedSection() {
        if (trainId==0){
            System.out.println("Lok " + trainId + " gibt anderen Zug eine Fahrtberechtigung");
            aviablePassesForTrain1.release();
            System.out.println("Lok " + trainId + " hat kritischen Streckenabschnitt freigegeben.");
            mutexOfSharedTrack.release();

        } else if (trainId==1){

            System.out.println("Lok " + trainId + " hat kritischen Streckenabschnitt freigegeben.");
            mutexOfSharedTrack.release();

        }

    }
}
