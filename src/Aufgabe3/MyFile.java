package Aufgabe3;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

/*
    MyFile-Klasse zur tatsächlichen Verarbeitung der READ-/WRITE-Operationen

    Ich habe diese Klasse weiterverwendet,
    weil sie die Action-Objekte der Threads verarbeiten kann und gut getestet ist

    Hier ist nur das Nötigste kommentiert, weil für die konkrete Umsetzung dieser Aufgabe
    die Klasse irrelevant ist (siehe bereit gestellte Lösung für Lese- und Schreibe-Klasse in Moodle)
 */
public class MyFile {

    // PATH muss abgeändert werden
    private static String PATH = "/Users/I569702/Documents/AdvancedIT/Advanced_IT_Testataufgaben/";

    // Schreiben einer bestimmten Zeile
    public static void writeFileLine(String fileName, int fileLine, String newLineContent) throws IndexOutOfBoundsException {
        try{
            readFile(fileName);
        } catch (FileNotFoundException e){
            System.out.println(e);
            try {
                List<String> emptyFIleContent = new ArrayList<>();
                writeFile(fileName, emptyFIleContent);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {

            if (fileLine<0){
                throw new IndexOutOfBoundsException();
            }

            List<String> fileLines = readFile(fileName);

            if (fileLines.size() < fileLine + 1){
                System.out.println("New lines have to be created");
                for (int i = fileLines.size(); i < fileLine; i++) {
                    fileLines.add("");
                }
                fileLines.add(newLineContent);
            } else {
                fileLines.set(fileLine, newLineContent);
            }

            writeFile(fileName,fileLines);


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    // Lesen einer bestimmten Zeile
    public static String readFileLine(String filename, Integer lineToRead){

        try {
            if (lineToRead<0){
                throw new IndexOutOfBoundsException();
            }

            List<String> fileContent = readFile(filename);
            if(fileContent != null) {

                if (fileContent.size() - 1 < lineToRead){
                    throw new IndexOutOfBoundsException();
                }

                String lineValue = fileContent.get(lineToRead);

                return lineValue;


            }

            return "An error occurred";
        } catch (FileNotFoundException e){
            System.out.println(e);
            return "File " + PATH + filename + " not found";
        } catch (IndexOutOfBoundsException e){
            System.out.println(e);
            return "Line not found";
        }
    }

    // Schreiben einer Datei
    // Hier werden für Testfälle künstliche Verzögerungen erzeugt
    private static void writeFile(String fileName, List<String> fileLines) throws IOException {

        PrintWriter printWriter = new PrintWriter(new FileWriter(PATH + fileName, false));

        Iterator iterator = fileLines.iterator();
        while (iterator.hasNext()){
            printWriter.println(iterator.next());
        }
        //Künstliche Verzögerung für Testfälle:
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        printWriter.flush();
    }


    // Einlesen einer Datei
    // Hier werden für Testfälle künstliche Verzögerungen erzeugt
    private static List<String> readFile(String name) throws FileNotFoundException {

            //Es wird versucht die Datei mit dem Schlüsselnamen zu finden, auszulesen und (wie verlangt) zurückgegeben
            List<String> fileContent = new BufferedReader(new FileReader(PATH + name)).lines().collect(Collectors.toList());
            System.out.println("Content from " + name + " loaded.");

            //Künstliche Verzögerung für Testfälle:
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return fileContent;
    }

}
