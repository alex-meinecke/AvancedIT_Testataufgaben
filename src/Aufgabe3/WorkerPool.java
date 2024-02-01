package Aufgabe3;

import java.util.Objects;

public class WorkerPool {

    // Zahl der Worker, die max. im Pool sein können
    // 8 Threads haben sich auf meinem Mac als schnellste Variante bewährt
    private final int NUMBER_OF_WORKERS = 8;

    // Array mit allen Workern wird initialisiert
    private Worker[] allWorkers = new Worker[NUMBER_OF_WORKERS];
    // WorkerPool-Array wird deklariert
    private Worker[] workerPool;

    // Pointer der nächsten leeren oder vollen Postion stehen im Ringpuffer beide anfangs auf null
    private int nextFullPointer = 0;
    private int nextEmptyPointer = 0;

    // Initialisierung des Lese- und Schreibemonitors
    private ReaderWriterMonitor readerWriterMonitor = new ReaderWriterMonitor();

    WorkerPool(){
        // Bei der Initialisierung des Workerpools werden alle 8 Worker initialisiert
        for (int i = 0; i < NUMBER_OF_WORKERS ; i++) {
            allWorkers[i] = new Worker(this);
        }

        workerPool = allWorkers;
    }

    private Boolean isWorkerPoolEmpty(){
        // Der RoundedBuffer ist dann leer, wenn
        // 1. beide Pointer auf der gleichen Position sind
        // 2. und gleichzeitig der Pointer für den nächsten vollen Platz auf einen leeren Slot zeigt
        if (nextEmptyPointer == nextFullPointer){
            if (workerPool[nextFullPointer] == null){
                return true;
            }
        }
        return false;
    }

    // 1. Die Action auf einen bestimmten File wird für den Lese- und Schreibemonitor registriert
    // 2. Wenn der Lese- und Schreibemonitor die Action auf den bestimmten File zulässt,
    //    wird der nächste freie Worker aus dem Pool geladen und zurückgegeben
    public Worker getNextFreeWorkerFromPoolAndRegisterAction(Action requiredAction){
        System.out.println("Registering operation: " + requiredAction.getCommand() + " on file " + requiredAction.getContent()[0]);

        // Jeweilige Registrierung von READ oder WRITE auf einen bestimmten File
        if (Objects.equals(requiredAction.getCommand(), "READ")){
            readerWriterMonitor.startReading(requiredAction.getContent()[0]);
        } else if (Objects.equals(requiredAction.getCommand(), "WRITE")){
            readerWriterMonitor.startWriting(requiredAction.getContent()[0]);
        }

        // Wenn Registrierung erfolgreich, wird der nächste Worker aus dem Pool geladen
        // (synchronized-Abschnitt muss ausgelagert werden, weil im Falle eines Warte-Events des Lese- und Schreibemonitors
        // der Monitor für den RoundedBuffer ein DeadLock verursacht
        Worker workerToBeReturned = getNextFreeWorkerFromPool();
        System.out.println("Returning free worker from worker pool for operation: " + requiredAction.getCommand());

        // Rückgabe
        return workerToBeReturned;
    }

    private synchronized Worker getNextFreeWorkerFromPool(){
        // Falls der Worker Pool leer sein sollte, wird gewartet
        while (isWorkerPoolEmpty()){
            try {
                System.out.println("Worker Pool is empty. Waiting for worker...");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Nächsten Worker laden, aus dem Worker Pool austragen und den Pointer um eins verändern
        Worker workerToBeReturned = workerPool[nextFullPointer];
        workerPool[nextFullPointer] = null;

        nextFullPointer = getNextIndexInRoundedBufferWorkerPool(nextFullPointer);


        return workerToBeReturned;
    }


    // Abmeldung eines READ- oder WRITE Events auf eine Datei
    // Wiedereintragen des Workers in den WorkerPool und der waitingQueue des Monitors Bescheid geben
    public synchronized void addFreeWorkerAndUnregisterAction(Worker finishingWorker){
        // Extrahieren der Action und des Command vom Worker
        Action actionToBeFinished = finishingWorker.getCurrentAction();
        String commandToBeFinished = actionToBeFinished.getCommand();

        // Jeweilige Abmeldung über den Lese- und Schreibemonitor einer READ- oder WRITE-Operation auf einen bestimmten File
        System.out.println("Unregistering action: " + commandToBeFinished + " on file " + actionToBeFinished.getContent()[0]);
        if (commandToBeFinished.equals("READ")){
            readerWriterMonitor.endReading(actionToBeFinished.getContent()[0]);
        } else if (commandToBeFinished.equals("WRITE")){
            readerWriterMonitor.endWriting(actionToBeFinished.getContent()[0]);
        }

        // Wiedereintragen des Workers an den nächsten freien Index und Pointer um eins verschieben
        // Wichtig: Hier muss nicht überprüft werden, ob der Workerpool schon voll ist, da es nicht mehr als 8 Worker seinen können
        workerPool[nextEmptyPointer] = finishingWorker;
        nextEmptyPointer = getNextIndexInRoundedBufferWorkerPool(nextEmptyPointer);
        System.out.println("Worker has been added back to pool (and the next waiting server tasks will be resumed)");

        //Benachrichtigung aller wartenden Operationen, im Falle das der Worker Pool leer war
        notifyAll();
    }

    private int getNextIndexInRoundedBufferWorkerPool(int currentIndex){
        // Wegen RoundedBuffer können Indizes nur zwischen 0-7 groß sein
        return (currentIndex + 1) % NUMBER_OF_WORKERS;
    }

}