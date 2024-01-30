package Aufgabe3;

public class WorkerPool {
    private static final int OPTIMAL_NUMBER_OF_WORKERS = 8;

    private Worker[] workers = new Worker[OPTIMAL_NUMBER_OF_WORKERS];

    private Worker[] roundedBufferPool;

    private int nextFreePositionInRoundedBuffer = 0;
    private boolean isFreePositionPointerBusy = false;
    private int nextFilledPositionInRoundedBuffer = 0;
    private boolean isFullPositionPointerBusy = false;


    private ReaderWriterMonitor readerWriterMonitor = new ReaderWriterMonitor();


    WorkerPool(){
        for (int i = 0; i < workers.length; i++) {
            //workers[i] = new Worker(this);
        }
        roundedBufferPool = workers;
    }

    //synchronized
    public Worker getNextFreeWorkerFromPool(Action action){

        System.out.println("Started to add following operation: " + action.getCommand());
        if (action.getCommand().equals("WRITE")){
            readerWriterMonitor.startWriting(action.getContent()[0]);
            System.out.println("WRITE ADDED!!!");
        } else if (action.getCommand().equals("READ")){
            readerWriterMonitor.startReading(action.getContent()[0]);
            System.out.println("READ ADDED!!!");
        }
        System.out.println("Checking if rounded buffer is empty.");
        //isRoundedBufferEmpty() |
        while (isFullPositionPointerBusy){
            try {
                System.out.println("Rounded buffer is empty. Waiting for new free worker.");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isFullPositionPointerBusy = true;

        System.out.println("Free worker in buffer is available.");
        Worker workerToBeReturned = roundedBufferPool[nextFilledPositionInRoundedBuffer];


        roundedBufferPool[nextFilledPositionInRoundedBuffer] = null;
        System.out.println("Selected free worker has been removed to rounded buffer.");

        nextFilledPositionInRoundedBuffer = nextIndexInRoundedBuffer(nextFilledPositionInRoundedBuffer);

        isFullPositionPointerBusy = false;

        //notifyAll();
        return workerToBeReturned;
    }

    //synchronized
    public void addFreeWorker(Worker newFreeWorker){


        Action workerAction = newFreeWorker.getCurrentAction();
        System.out.println("Operation ends: " + workerAction.getCommand());

        if (workerAction.getCommand().equals("READ")) {
            readerWriterMonitor.endReading(workerAction.getContent()[0]);
        } else if (workerAction.getCommand().equals("WRITE")) {
            readerWriterMonitor.endWriting(workerAction.getContent()[0]);
        }

        while (isFreePositionPointerBusy) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        roundedBufferPool[nextFreePositionInRoundedBuffer] = newFreeWorker;
        System.out.println("New free Worker has been added to rounded buffer");
        nextFreePositionInRoundedBuffer = nextIndexInRoundedBuffer(nextFreePositionInRoundedBuffer);

        isFreePositionPointerBusy = false;

        System.out.println("Notify all ServerTask-operations that a new free worker has been added.");
        //notifyAll();
    }

    private int nextIndexInRoundedBuffer(int currentIndex){
        return (currentIndex + 1) % 8;
    }
    public boolean isRoundedBufferEmpty(){
        if(nextFilledPositionInRoundedBuffer==nextFreePositionInRoundedBuffer){
            if(roundedBufferPool[nextFilledPositionInRoundedBuffer] == null){
                return true;
            } else {
                return false;
            }

        } else return false;

    }

}
