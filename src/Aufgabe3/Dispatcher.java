package Aufgabe3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Dispatcher implements Runnable {
    private WorkerPool2 workerPool = new WorkerPool2();
    private ReaderWriterMonitor readerWriterMonitor = new ReaderWriterMonitor();

    public void addServerTask(DatagramPacket newClientMessage, DatagramSocket datagramSocket){
        ServerTask newServerTask = new ServerTask(newClientMessage, datagramSocket);

        dispatch(newServerTask);
    }

    private void dispatch(ServerTask serverTask){
        String extractedMessage = new String(serverTask.getClientMessage().getData(), 0, serverTask.getClientMessage().getLength());
        Action currentAction = Action.generateAction(extractedMessage);

        System.out.println("Getting new free Worker from Worker Pool");
        Worker nextFreeWorker = workerPool.getNextFreeWorkerFromPoolAndRegisterAction(currentAction);

        System.out.println("Dispatching server task to returned free worker");
        nextFreeWorker.setServerTask(serverTask);
        new Thread(nextFreeWorker).start();
        System.out.println("Task dispatched");
    }

    @Override
    public void run() {


    }
}
