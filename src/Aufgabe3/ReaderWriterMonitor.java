package Aufgabe3;

import java.util.*;

public class ReaderWriterMonitor {

    private List<String> filesCurrentlyUsedForReading = new Vector<>();
    private List<String> filesCurrentlyUsedForWriting = new Vector<>();

    public synchronized void startReading(String fileName){
        System.out.println("Trying to start reading operation for " + fileName);
        while (isFileWriterActive(fileName)){
            System.out.println("A prioritized writing operation is currently executed on " + fileName + ". Waiting for finishing.");
            try {
                System.out.println("Waiting for writer on file " + fileName + " to finish.");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        filesCurrentlyUsedForReading.add(fileName);
        System.out.println("Reading operation on " + fileName + " is registered.");
    }

    public synchronized void endReading(String fileName){
        filesCurrentlyUsedForReading.remove(fileName);
        System.out.println("Reading operation on " + fileName + " is deleted. All waiting operations are getting notified.");
        notifyAll();
    }

    public synchronized void startWriting(String fileName){
        System.out.println("Trying to start writing operation for " + fileName);

        while (isFileReaderActive(fileName) | isFileWriterActive(fileName)){
            // writing or
            System.out.println("A prioritized reading operation is currently executed on " + fileName + ". Waiting for finishing...");
            try {
                System.out.println("Waiting for writer or reader on file " + fileName + " to finish.");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        filesCurrentlyUsedForWriting.add(fileName);
        System.out.println("Writing operation on " + fileName + " is registered.");
    }

    public synchronized void endWriting(String fileName){
        filesCurrentlyUsedForWriting.remove(fileName);
        System.out.println("Writing operation on " + fileName + " is deleted. All waiting operations are getting notified.");
        notifyAll();
    }

    public synchronized Boolean isFileWriterActive(String fileName){
        return filesCurrentlyUsedForWriting.contains(fileName);
    }

    public synchronized Boolean isFileReaderActive(String fileName){
        return filesCurrentlyUsedForReading.contains(fileName);
    }

}
