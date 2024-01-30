package Aufgabe3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Worker implements Runnable {

    private DatagramPacket message;
    private DatagramSocket socket;

    private Action currentAction;

    WorkerPool2 currentWorkerPool;

    //private ReaderWriterMonitor readerWriterMonitor = new ReaderWriterMonitor();

    Worker(WorkerPool2 currentWorkerPool){
        this.currentWorkerPool = currentWorkerPool;
        //this.readerWriterMonitor = readerWriterMonitor;
    }

    Worker(DatagramPacket message, DatagramSocket socket, WorkerPool currentWorkerPool2){
        this.currentWorkerPool = currentWorkerPool;
        this.message = message;
        this.socket = socket;
    }

    public void setServerTask(ServerTask serverTask) {
        this.message = serverTask.getClientMessage();
        this.socket = serverTask.getServerDatagramSocket();
    }

    public Action getCurrentAction(){
        return currentAction;
    }

    @Override
    public void run() {
        System.out.println("Worker has started to process ServerTask");

        String extractedMessage = new String(message.getData(), 0, message.getLength());

        System.out.println("Message from client: " + extractedMessage);

        currentAction = Action.generateAction(extractedMessage);

        String text = MyFile.handleAction(currentAction);


        System.out.println("Worker prepared message to client: " + text);
        DatagramPacket datagramPacket = new DatagramPacket(text.getBytes(), text.length(), message.getAddress(), message.getPort());

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
