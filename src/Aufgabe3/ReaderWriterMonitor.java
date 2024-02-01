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
            try {
                System.out.println("READ-Operation waits for writer on file " + fileName + " to finish.");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // Ein Dateiname wird für Leseoperation registriert
        // (Es können mehrmals die gleichen Dateinamen in der Liste sein,
        // da mehrere Leseoperationen zugleich auf einer Datei ausgeführt werden können)
        filesCurrentlyUsedForReading.add(fileName);
        System.out.println("READ-operation on " + fileName + " is registered and is allowed to start.");
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

        // Falls zufällig noch eine andere Schreibeoperation aktiv ist,
        // muss der Schreiber warten, um Datenkonsistenz zu wahren
        while (isFileWriterActive(fileName)){
            try {
                System.out.println("WRITE-Operation waits for writer on file " + fileName + " to finish.");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Registrierung des Schreibeprozesses
        // Ab jetzt können keine weiteren Leseprozesse registriert werden
        filesCurrentlyUsedForWriting.add(fileName);

        // Falls zufällig noch aktive Leseoperationen auf die Datei registriert sind,
        // muss der Schreiber warten, um Datenkonsistenz zu wahren
        while (isFileReaderActive(fileName)){
            try {
                System.out.println("WRITE-Operation waits for reader on file " + fileName + " to finish.");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("WRITE-Operation on " + fileName + " is registered and is allowed to start.");
    }

    // Beenden eines Schreibe-Prozesses
    public synchronized void endWriting(String fileName){
        // Dateiname für aktiven Schreibeprozess wird aus der Liste ausgetragen
        filesCurrentlyUsedForWriting.remove(fileName);
        System.out.println("Writing operation on " + fileName + " is deleted. All waiting operations are getting notified.");
        notifyAll();
    }


    // isActive-Methoden prüfen, ob sich jeweils registrierte WRITE- oder READ-Operationen
    // in den jeweiligen Listen befinden
    public synchronized Boolean isFileWriterActive(String fileName){
        return filesCurrentlyUsedForWriting.contains(fileName);
    }

    public synchronized Boolean isFileReaderActive(String fileName){
        return filesCurrentlyUsedForReading.contains(fileName);
    }

}