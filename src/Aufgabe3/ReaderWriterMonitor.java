package Aufgabe3;

import java.util.*;

public class ReaderWriterMonitor {

    // Zwei Listen für Dateien, auf die gerade eine READ oder WRITE-Operation ausgeführt werden,
    // werden als 'Vector' initialisiert (Zusätzliche Thread sicherheit)
    private List<String> filesCurrentlyUsedForReading = new Vector<>();
    private List<String> filesCurrentlyUsedForWriting = new Vector<>();

    // Registrierung eines Lese-Prozesses
    public synchronized void startReading(String fileName){
        System.out.println("Trying to start reading operation for " + fileName);
        // Wenn gerade ein Schreiber auf der Datei registriert ist, muss der Leser warten (SCHREIBERPRIORITÄT)
        while (isFileWriterActive(fileName)){
            System.out.println("A prioritized writing operation is currently executed on " + fileName + ". Waiting for finishing.");
            try {
                System.out.println("Waiting for writer on file " + fileName + " to finish.");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // Ein Dateiname wird für Leseoperation registriert
        // (Es können mehrmals die gleichen Dateinamen in der Liste sein,
        // da mehrere Leseoperationen zugleich auf einer Datei ausgeführt werden können)
        filesCurrentlyUsedForReading.add(fileName);
        System.out.println("Reading operation on " + fileName + " is registered.");
    }

    // Beenden eines Lese-Prozesses
    public synchronized void endReading(String fileName){
        // Ein Dateiname wird für Leseoperation ausgetragen
        // (Es können mehrmals die gleichen Dateinamen in der Liste sein,
        // da mehrere Leseoperationen zugleich auf einer Datei ausgeführt werden können)
        filesCurrentlyUsedForReading.remove(fileName);
        System.out.println("Reading operation on " + fileName + " is deleted. All waiting operations are getting notified.");

        // Alle Schreiber, die blockiert sind, weil aktuell gerade noch eine Leseoperation auf die Datei registriert ist,
        // können starten
        notifyAll();
    }

    // Registrierung eines Schreibe-Prozesses
    public synchronized void startWriting(String fileName){
        System.out.println("Trying to start writing operation for " + fileName);

        // Falls
        // !!! a) zufällig noch eine Leseoperation auf der Datei registriert ist () oder
        // b) zufällig noch eine andere Schreibeoperation aktiv ist
        // muss der Schreiber warten um Datenkonsistenz zu wahren

        //tep. removed: isFileReaderActive(fileName) |
        while (isFileWriterActive(fileName)){
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

    // Beenden eines Schreibe-Prozesses
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
