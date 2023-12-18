package Aufgabe1;

// Setting für a) und b) wird aus der Aufgabenstellung übernommen
abstract public class Train implements Runnable {

    protected Integer trainId;

    private Integer speedInMPS = (int) ((Math.random() * (100 - 10)) + 100);
    private Integer privateTrackLengthInMeters = (int) ((Math.random() * (1500 - 50)) + 1500);
    Integer timeToPass = (int) (privateTrackLengthInMeters/speedInMPS);



    public Train(Integer trainId){
        this.trainId = trainId;

    }

    public void run() {
        while (true) {
            driveOnPrivateTrack();

            enterSharedSection();
            System.out.println("Lok " +  trainId + " fährt innerhalb des kritischen Abschnittes");

            exitSharedSection();
        }
    }

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

    public void setTimeToPassPrivateTrainTrackInSeconds(Integer timeToPass) {
        this.timeToPass = timeToPass;
    }
}
