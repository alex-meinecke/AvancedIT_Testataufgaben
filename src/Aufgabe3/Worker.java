package Aufgabe3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Worker implements Runnable {

    private Action currentAction;

    private WorkerPool currentWorkerPool;

    Worker(WorkerPool currentWorkerPool){
        this.currentWorkerPool = currentWorkerPool;
    }

    public void setAction(Action newAction){
        this.currentAction = newAction;
    }

    public Action getCurrentAction(){
        return currentAction;
    }

    @Override
    public void run() {
        System.out.println("Worker has started to process Action: " + currentAction.getCommand());

        String response = MyFile.handleAction(currentAction);

        System.out.println("Worker prepared message for client: " + response);
        DatagramPacket datagramPacket = new DatagramPacket(response.getBytes(), response.length(), currentAction.getClientAddress(), currentAction.getClientPort());

        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(datagramPacket);
            System.out.println("Response has been sent");

        } catch (IOException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }

        System.out.println("Worker has finished and is adding himself back to the worker pool");
        currentWorkerPool.addFreeWorkerAndUnregisterAction(this);
    }


}
