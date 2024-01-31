package Aufgabe3;

public class ActionHandler {

    // Action wird verarbeitet
    public static String handleFileAction(Action currentAction) {
        // Standard-Zeile, fall Eingabe ungültig (z.B. READ test,eins)
        int fileLine = 0;

        // Dateiname und Kommando extrahieren
        String fileName = currentAction.getContent()[0];
        String command = currentAction.getCommand();

        try {
            // Versuch die Standardzeile mit der Zeile aus der Clientnachricht zu überschreiben
            fileLine = Integer.valueOf(currentAction.getContent()[1].trim()) -1;
        } catch (NumberFormatException e) {
            System.err.println(e);
        } catch (IndexOutOfBoundsException e){
            System.out.println(e);
        }

        switch (command) {
            case "READ":
                // Im Falle von Read-Kommando wird gelesen und Ergebnis zurückgegeben
                String result =  MyFile.readFileLine(fileName, fileLine);
                return result;
            case "WRITE":
                // Im Falle von WRITE-Kommando wird gelesen und Ergebnis zurückgegeben
                try {
                    // Versuch, die dritte Stelle des Contents zu extrahieren (neuer Zeileninhalt)
                    // Bspw. WRITE test,1,NEUER ZEILENINHALT
                    String newLineContent = currentAction.getContent()[2];

                    // Weiterverarbeitung
                    MyFile.writeFileLine(fileName, fileLine, newLineContent);

                    return "New line content: " + newLineContent;
                } catch (ArrayIndexOutOfBoundsException e){
                    return "Line index not found";
                } catch (IndexOutOfBoundsException e){
                    return "Line index out of range";
                }
            // Falls Client ungültiges Kommando gibt, wird Fehlermeldung zurückgegeben
            default: return "Command not defined.";
        }
    }

}
