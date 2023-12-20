package Aufgabe1.b;

import java.util.concurrent.Semaphore;

/*
    Train0 definiert spezielle Logik für Lok 0:
    - zentraler Unterschied Lok 0 fährt immer vor Lok 1, darf aber nicht mehr als eine Durchfahrt im Vorsprung liegen

    Hinweis: Die print-Ausgaben kommen teilweise in ihrem Detail Kommentare gleich,
    deshalb sind nur dort extra Kommentare vorhanden, wo eine genauere Erklärung notwendig ist
 */
public class Train0 extends TrainDraftForExerciseB {

    public Train0(TrainDraftForExerciseB[] trains, Boolean[] isWaiting, Semaphore[] privateSemaphoresForTrains, Semaphore mutexForSharedData, Boolean isSharedTrackAreaBusy) {
        super(train0Id, trains, isWaiting, privateSemaphoresForTrains, mutexForSharedData, isSharedTrackAreaBusy);
    }

    @Override
    protected void enterSharedSection() {
        try {
            System.out.println("Lok " + trainId + " fragt Zugriff auf geteilte Daten an.");
            mutexForSharedData.acquire();

            System.out.println("Lok " + trainId + " prüft ob der geteilte Streckenabschnitt frei ist und ob sie erstmal Lok " + train1Id + " vorlassen muss.");
            //Freigabe nur wenn: a) der geteilte Schienenbereich überhaupt frei ist UND b) nachdem Lok1 gefahren ist, müssen beide gleich oft durch den geteilten Abschnitt gefahren sein
            if (!isSharedTrackAreaBusy && (numberOfDrivesThroughTheSharedArea-trains[train1Id].numberOfDrivesThroughTheSharedArea)==0){
                System.out.println("Lok " + trainId + " darf fahren und gibt ihren privaten Semaphore eine Durchfahrtsbrechtigung.");
                privateSemaphoresForTrains[trainId].release();
            } else {
                System.out.println("Lok " + trainId + " darf nicht fahren und wartet nun.");
                isWaiting[trainId] = true;
            }

            System.out.println("Lok " + trainId + " gibt Zugriff auf geteilte Daten wieder frei.");
            mutexForSharedData.release();

            //System.out.println("Lok" + trainId + " startet Durchfahrtsanfrage und muss ggf. warten.");
            privateSemaphoresForTrains[trainId].acquire();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void exitSharedSection() {
        numberOfDrivesThroughTheSharedArea++;
        System.out.println("Lok " + trainId + " hat nach erfolgreicher Durchfahrt ihren Durchfahrtszähler auf " + numberOfDrivesThroughTheSharedArea + " erhöht.");

        try {
            System.out.println("Lok " + trainId + " fragt Zugriff auf geteilte Daten an.");
            mutexForSharedData.acquire();

            System.out.println("Lok " + trainId + " prüft ob Lok " + train1Id + " wartet");
            if (isWaiting[train1Id]) {
                System.out.println("Lok " + trainId + " schickt Lok " + train1Id + " das Signal zur weiterfahrt");
                isWaiting[train1Id] = false;
                privateSemaphoresForTrains[train1Id].release();
            } else {
                System.out.println("Lok " + trainId + " gibt die geteilte Strecke wieder frei, da Lok " + train1Id + " nicht wartet");
                isSharedTrackAreaBusy = false;
            }

            System.out.println("Lok " + trainId + " gibt Zugriff auf geteilte Daten wieder frei.");
            mutexForSharedData.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
