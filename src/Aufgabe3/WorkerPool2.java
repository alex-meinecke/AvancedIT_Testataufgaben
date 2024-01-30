package Aufgabe3;

import java.util.Objects;

public class WorkerPool2 {

    private final int NUMBER_OF_WORKERS = 8;

    private Worker[] allWorkers = new Worker[NUMBER_OF_WORKERS];
    private Worker[] workerPool;

    private int nextFullPointer = 0;
    private int nextEmptyPointer = 0;

    private ReaderWriterMonitor readerWriterMonitor = new ReaderWriterMonitor();

    WorkerPool2(){
        for (int i = 0; i < NUMBER_OF_WORKERS ; i++) {
            allWorkers[i] = new Worker(this);
        }

        workerPool = allWorkers;
    }

    private Boolean isWorkerPoolEmpty(){
        if (nextEmptyPointer == nextFullPointer){
            if (workerPool[nextFullPointer] == null){
                return true;
            }
        }

        return false;
    }

    public Worker getNextFreeWorkerFromPoolAndRegisterAction(Action requiredAction){
        System.out.println("Registering operation: " + requiredAction.getCommand() + " on file " + requiredAction.getContent()[0]);

        if (Objects.equals(requiredAction.getCommand(), "READ")){
            readerWriterMonitor.startReading(requiredAction.getContent()[0]);
        } else if (Objects.equals(requiredAction.getCommand(), "WRITE")){
            readerWriterMonitor.startWriting(requiredAction.getContent()[0]);
        }

        Worker workerToBeReturned = getNextFreeWorkerFromPool();
        System.out.println("Returning free worker from worker pool for operation: " + requiredAction.getCommand());

        return workerToBeReturned;
    }

    private synchronized Worker getNextFreeWorkerFromPool(){
        while (isWorkerPoolEmpty()){
            try {
                System.out.println("Worker Pool is empty. Waiting for worker...");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Worker workerToBeReturned = workerPool[nextFullPointer];
        workerPool[nextFullPointer] = null;

        nextFullPointer = getNextIndexInRoundedBufferWorkerPool(nextFullPointer);


        return workerToBeReturned;
    }



    public synchronized void addFreeWorkerAndUnregisterAction(Worker finishingWorker){
        Action actionToBeFinished = finishingWorker.getCurrentAction();
        String commandToBeFinished = actionToBeFinished.getCommand();

        System.out.println("Unregistering action: " + commandToBeFinished + " on file " + actionToBeFinished.getContent()[0]);
        if (commandToBeFinished.equals("READ")){
            readerWriterMonitor.endReading(actionToBeFinished.getContent()[0]);
        } else if (commandToBeFinished.equals("WRITE")){
            readerWriterMonitor.endWriting(actionToBeFinished.getContent()[0]);
        }

        workerPool[nextEmptyPointer] = finishingWorker;
        nextEmptyPointer = getNextIndexInRoundedBufferWorkerPool(nextEmptyPointer);
        System.out.println("Worker has been added back to pool (and the next waiting server tasks will be resumed)");

        notifyAll();
    }

    private int getNextIndexInRoundedBufferWorkerPool(int currentIndex){
        return (currentIndex + 1) % NUMBER_OF_WORKERS;
    }





}
