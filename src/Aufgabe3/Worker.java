package Aufgabe3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Worker implements Runnable {

    // Zähler, wie viele Worker es schon gibt
    private static int workerCounter = 0;

    // Für Identifikation in der Konsole für Testfälle
    private int identifier;

    // Getter für identifier
    public int getIdentifier(){
        return identifier;
    }

    // Aktuelle Action
    private Action currentAction;

    // Workerpool für die Selbstrückführung am Ende
    private WorkerPool currentWorkerPool;

    // Konstruktor
    Worker(WorkerPool currentWorkerPool){
        this.currentWorkerPool = currentWorkerPool;

        // Jeder Worker soll seinen eigenen Identifikator für Testfälle bekommen
        workerCounter++;
        this.identifier = workerCounter;
    }

    // Übergabe der zu bearbeitenden Action (wird vom Dispatcher gesetzt)
    public void setAction(Action newAction){
        this.currentAction = newAction;
    }

    // Rückgabe der aktuell zu bearbeitenden Action
    public Action getCurrentAction(){
        return currentAction;
    }

    // Ausführung zur 'Lebenszeit' eines Threads
    @Override
    public void run() {
        System.out.println("Worker " + identifier + " has started to process Action: " + currentAction.getCommand());

        // Generierung der Response mit der statischen MyFile-Klasse
        String response = ActionHandler.handleFileAction(currentAction);

        System.out.println("Worker " + identifier + " prepared message for client: " + response);
        // Vorbereitung des Antwortpakets an den Client
        DatagramPacket datagramPacket = new DatagramPacket(response.getBytes(), response.length(), currentAction.getClientAddress(), currentAction.getClientPort());

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            // Senden des Antwortpaketes
            datagramSocket.send(datagramPacket);
            System.out.println("Response has been sent");

        } catch (IOException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }

        System.out.println("Worker  " + identifier + " has finished and is adding himself back to the worker pool");
        // Selbstrückführung in den Workerpool
        currentWorkerPool.addFreeWorkerAndUnregisterAction(this);
    }


}
