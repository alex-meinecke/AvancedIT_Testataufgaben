package Aufgabe1;

/*
    TrainDraftForBothExercises definiert generelles, was alle Züge in Aufgabe a und b brauchen:
    - Interface Runnable
    - TrainId
    - Zufällige Dauer, die ein Zug auf seinem eigenen Teil zurücklegt
        - Überschreibung dieser Dauer für Testfälle
    - While-Schleife mit enter- und exit-Methode, wie sie in der Aufgabenstellung definiert wurden
 */

abstract public class TrainDraftForBothExercises implements Runnable {
    // Identifizierung des Zuges
    protected Integer trainId;

    //Zufällige Geschwindigkeit zischen 10 bis 100 Meter/s
    private Integer speedInMPS = (int) ((Math.random() * (100 - 10)) + 100);
    //Zufällige Bahnlänge zischen 50 bis 1000 Meter
    private Integer privateTrackLengthInMeters = (int) ((Math.random() * (1500 - 50)) + 1500);

    //Berechnung der Fahrzeit auf privater Strecke aus Bahnlänge/Geschwindigkeit
    Integer timeToPass = (int) (privateTrackLengthInMeters/speedInMPS);


    //Übergabe der TrainId
    public TrainDraftForBothExercises(Integer trainId){
        this.trainId = trainId;
    }

    /*
        Schleifen-Setting in run() für a) und b) wird aus der Aufgabenstellung übernommen
    */
    public void run() {
        while (true) {
            //Wartezeit, welche die Fahrt einer Lok auf ihrer eigenen Strecke simulieren soll
            driveOnPrivateTrack();

            //Gesicherten Bereich versuchen zu betreten
            enterSharedSection();
            System.out.println("Lok " +  trainId + " fährt innerhalb des kritischen Abschnittes");
            //Durchfahrt durch gesicherten Bereich dauert 2 Sekunden
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //Gesicherten Bereich verlassen
            exitSharedSection();
        }
    }

    /*
        Abwarten der Fahrzeit, die ein Zug auf seiner Strecke zurücklegt
        Verbleibende Zeit wird in Sekunden und % zurückgegeben
     */
    private void driveOnPrivateTrack() {
        for (int i = 1; i < timeToPass + 1; i++) {
            try {
                Thread.sleep(1000);
                System.out.println("Lok " + trainId + " fährt private Strecke ab. " +
                                    "Verbleibende Dauer: " + i + "s/" + timeToPass + "s " +
                                    "\t(" + (i*100/timeToPass) + "%)");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    protected abstract void enterSharedSection();

    protected abstract void exitSharedSection();

    //Überschreiben der zufälligen Fahrzeit auf privater Stecke für Testfälle
    public void setTimeToPassPrivateTrainTrackInSeconds(Integer timeToPass) {
        this.timeToPass = timeToPass;
    }
}
