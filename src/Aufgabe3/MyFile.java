package Aufgabe3;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class MyFile {

    private static String PATH = "/Users/I569702/Documents/AdvancedIT/Advanced_IT_Testataufgaben";

    public static String handleAction(Action currentAction) {
        int fileLine = 0;
        String fileName = currentAction.getContent()[0];
        String command = currentAction.getCommand();

        try {
            fileLine = Integer.valueOf(currentAction.getContent()[1].trim());
        } catch (NumberFormatException e) {
            System.err.println(e);
        } catch (IndexOutOfBoundsException e){
            System.out.println(e);
            fileLine = 0;
        }

        switch (command) {
            case "READ":
                String result =  readFileLine(fileName, fileLine);
                return result;
            case "WRITE":
                try {
                    String newLineContent = currentAction.getContent()[2];
                    writeFileLine(fileName, fileLine, newLineContent);

                    return "New line content: " + newLineContent;
                } catch (ArrayIndexOutOfBoundsException e){
                    return "Line index not found";
                }

            default: return "Command not defined.";
        }
    }

    private static void writeFileLine(String fileName, int fileLine, String newLineContent) {

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
            List<String> fileLines = readFile(fileName);

            if (fileLines.size() < fileLine + 1){
                System.out.println("Zeilen müssen erzeugt werden");
                for (int i = fileLines.size(); i < fileLine; i++) {
                    fileLines.add("");
                }
                fileLines.add(newLineContent);
            } else {
                fileLines.set(fileLine, newLineContent);
            }

            writeFile(fileName,fileLines);


        } catch (IndexOutOfBoundsException e){
            System.out.println("Line not found and has to be created");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

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
            return "File not found";
        } catch (IndexOutOfBoundsException e){
            System.out.println(e);
            return "Line not found";
        }
    }

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
