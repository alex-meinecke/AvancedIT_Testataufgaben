package Aufgabe1.b;

import java.util.concurrent.Semaphore;

/*
    Train1 definiert spezielle Logik für Lok 1:
    - zentraler Unterschied Lok 1 fährt immer nach 0

    Hinweis: Die print-Ausgaben kommen teilweise in ihrem Detail Kommentare gleich,
    deshalb sind nur dort extra Kommentare vorhanden, wo eine genauere Erklärung notwendig ist
 */
public class Train1 extends TrainDraftForExerciseB  {

    public Train1(TrainDraftForExerciseB[] trains, Boolean[] isWaiting, Semaphore[] privateSemaphoresForTrains, Semaphore mutexForSharedData, Boolean isSharedTrackAreaBusy) {
        super(train1Id, trains, isWaiting, privateSemaphoresForTrains, mutexForSharedData, isSharedTrackAreaBusy);
    }

    @Override
    protected void enterSharedSection() {
        try {
            System.out.println("Lok " + trainId + " fragt Zugriff auf geteilte Daten an.");
            mutexForSharedData.acquire();

            System.out.println("Lok " + trainId + " prüft ob der geteilte Streckenabschnitt frei ist und ob sie erstmal Lok " + train0Id + " vorlassen muss.");
            //Freigabe nur wenn: a) der geteilte Schienenbereich überhaupt frei ist UND b) Lok1 Lok0 nicht in der Reihenfolge überholen würde (Lok 0 hat Vorrang)
            if ((!isSharedTrackAreaBusy && (numberOfDrivesThroughTheSharedArea < trains[train0Id].numberOfDrivesThroughTheSharedArea))){
                System.out.println("Lok " + trainId + "darf fahren und gibt ihren privaten Semaphore eine Durchfahrtsbrechtigung.");
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

            System.out.println("Lok " + trainId + " prüft ob Lok " + train0Id + " wartet");
            if (isWaiting[train0Id]){
                System.out.println("Lok " + trainId + " schickt Lok " + train0Id + " das Signal zur weiterfahrt");
                isWaiting[train0Id] = true;
                privateSemaphoresForTrains[train0Id].release();
            } else {
                System.out.println("Lok " + trainId + " gibt die geteilte Strecke wieder frei, da Lok " + train0Id + " nicht wartet");
                isSharedTrackAreaBusy = false;
            }

            System.out.println("Lok" + trainId + " gibt Zugriff auf geteilte Daten wieder frei.");
            mutexForSharedData.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
