package Aufgabe3;

import java.net.DatagramPacket;

public class Dispatcher {

    //Generieren eines Workerpools
    private WorkerPool workerPool = new WorkerPool();

    //Anmeldung einer neuen Aufgabe (Action)
    public void addNewServerTask(DatagramPacket newClientMessage){
        // Generierung einer Action aus der Clientnachricht
        Action currentAction = Action.generateAction(newClientMessage);

        //Übergabe für Weiterverarbeitung
        dispatch(currentAction);
    }

    // Übergabe an einem Worker aus dem Workerpool
    private void dispatch(Action newAction){
        System.out.println("Getting new free worker from worker pool");
        // Laden des nächsten freien Workers aus dem Worker pool
        Worker nextFreeWorker = workerPool.getNextFreeWorkerFromPoolAndRegisterAction(newAction);

        System.out.println("Dispatching action and client information to returned free worker");
        // Übergeben der neuen Action an den Worker zur Abarbeitung
        nextFreeWorker.setAction(newAction);

        // Starten des Workerthreads
        new Thread(nextFreeWorker).start();
        System.out.println("Dispatched");
    }

}
