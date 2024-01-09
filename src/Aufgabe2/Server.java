package Aufgabe2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Server {
    // Definierter Port
    public static final int PORT = 7777;
    // PrintWriter als Outputstream
    private static PrintWriter outputStream;
    // BufferedInputStream als Inputstream
    private static BufferedInputStream inputStream;
    // BufferedReader zum Lesen der Clientnachrichten
    private static BufferedReader reader;
    // ServerSocket
    private static ServerSocket serverSocket;
    // Socket des Clients
    private static Socket connectedClient;

    // Commands festgelegt
    private static final String SAVE_COMMAND = "SAVE";
    private static final String GET_COMMAND = "GET";

    // (Mein persönlicher) Path definiert. => Wie empfohlen auf dem Desktop
    // Achtung: Für die Überprüfung muss dieser natürlich geändert werden!
    private static String PATH_FOR_MESSAGE_FILE = "/Users/I569702/Desktop/message_folder/";

    public static void main(String[] args) {
        // Wenn Sie ihren eigenen Ordner erstellt haben, bitte das Verzeichnis eingeben als erster Parameter in der Console eingeben.
        if (args != null && args.length > 0) {
            //Standard wird überschieben
            PATH_FOR_MESSAGE_FILE = args[0];
            System.out.println("Overwriting path to " + PATH_FOR_MESSAGE_FILE);
        }

        try {
            // Server einmalig starten
            System.out.println("Starting Server");
            serverSocket = new ServerSocket(PORT);
            while (true) {
                try {
                    // Warten, bis sich der Client verbindet
                    System.out.println("Waiting for client");
                    connectedClient = serverSocket.accept();
                    System.out.println("Client connected");

                    // Input Stream mit Input von Client verbinden und Stream via BuffedReader lesbar machen
                    inputStream = new BufferedInputStream(connectedClient.getInputStream());
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    // Warten, bis der Client eine Nachricht schickt
                    System.out.println("Waiting for message from client");
                    outputStream = new PrintWriter(connectedClient.getOutputStream());
                    String clientMessage = reader.readLine();

                    System.out.println("Message form client: " + clientMessage);

                    // Verarbeitung der in der Nachricht vorhandenen Parameter
                    // Was ist der Command? (Erstes Wort)
                    // Was ist der Content? (Alles nach erstem Wort)
                    String[] messageParameters = splitMessageInToAnArrayOfParameters(clientMessage);
                    String command = extractMessageCommand(messageParameters);
                    String content = extractMessageContent(messageParameters, clientMessage, command);

                    // Verarbeitet Command und Content und bereitet die jeweils passende Response vor.
                    // (Response in der Aufgabenstellung definiert)
                    String generatedResponse = executeCommandAndGenerateResponse(command, content);

                    // Response an den Client zurückgeben
                    outputStream.println(generatedResponse);
                    outputStream.flush();

                } catch (IOException e) {
                    // ggf. Fehlerausgabe
                    System.err.println(e);
                } finally {
                    // Am Ende jeder Bearbeitung der TCP-Verbindung trennen
                    // Umsetzung der Non-Persistence
                    System.out.println("Disconnecting current client");
                    try {
                        connectedClient.close();
                        outputStream.close();
                        inputStream.close();

                    } catch (IOException e) {
                        // ggf. Fehlerausgabe
                        System.err.println(e);
                    }
                    System.out.println("Client disconnected");
                    System.out.println("====================================");
                }

            }

        } catch (IOException e){System.out.println(e);}

    }

    private static String[] splitMessageInToAnArrayOfParameters(String clientMessage) {
        String[] messageParameters;

        if (clientMessage==null) {
            // Falls der Client null schicken sollte, wird dies in einen Command ohne Zeichen umgewandelt,
            // damit spätere Weiterverarbeitung funktioniert
            //
            // (z.B. TCP-Verbindung verloren -> Inputstream bricht auch ab -> clientMessage == null)

            System.out.println("Correction of the client message: null is now a parameter without chars");
            messageParameters = new String[1];
            messageParameters[0] = "";

        } else {
            // Client Message wird zur Weiterverarbeitung in Parameter aufgeteilt
            messageParameters = clientMessage.split(" ");
        }
        return messageParameters;
    }

    private static String executeCommandAndGenerateResponse(String command, String content) throws IOException {
        // Verarbeitet Command und Content und bereitet die jeweils passende Response vor.
        // (Response in der Aufgabenstellung definiert)


        if (Objects.equals(command, SAVE_COMMAND)) {
            // Wenn der Command SAVE gefragt ist:

            // Es wird eine zufällige Zahl als Schlüssel (fileName) erstellt
            Integer fileName = (int) (1000000000 * Math.random());



            try {
                // Es wird eine Datei mit dem Zufallsnamen in dem oben definierten Ordner erstellt
                new PrintWriter(new FileWriter(PATH_FOR_MESSAGE_FILE + fileName), true).println(content);
                System.out.println("File " + fileName + " generated with following content: " + content);

                // Das Ergebnis wird zurückgeben (wie verlangt)
                return "KEY " + fileName;
            } catch (IOException e) {
                // Fehlerbehandlung, falls es Probleme beim Erstellen der Datei gab

                System.err.println(e);
                System.out.println("Error while generating file. Please check if PATH (" +  PATH_FOR_MESSAGE_FILE + ") exists or access permission is granted");

                // Hier wäre eine andere Response für den Client vielleicht besser gewesen
                // Ich habe mich an der Aufgabenstellung orientiert: "[...] Anderenfalls sendet er: FAILED [...]" (siehe T-4)
                return "FAILED";
            }

        } else if ((Objects.equals(command, GET_COMMAND))) {
            // Wenn der Command GET gefragt ist:

            try {
                //Es wird versucht die Datei mit dem Schlüsselnamen zu finden, auszulesen und (wie verlangt) zurückgegeben
                String fileContent = new BufferedReader(new FileReader(PATH_FOR_MESSAGE_FILE + content)).readLine();
                System.out.println("Message " + content + " loaded: " + fileContent);
                return "OK " + fileContent;
            } catch (IOException e) {
                // Wenn die Datei nicht existiert, wird das auch (wie verlangt) zurückgegeben
                System.out.println("Error while reading file.");

                return "FAILED";
            }
        } else {
            // Zusätzlich: Wenn ein Command nicht definiert ist, wird auch das an den Client weitergeben
            System.out.println("Command " + "\"" + command + "\" unknown");
            return "Command " + "\"" + command + "\" unknown";
        }
    }

    private static String extractMessageContent(String[] messageParameters, String clientMessage, String command) {
        // Wenn es neben dem Command noch mehr Daten in der Message geben sollte (messageParameters.length > 1),
        // wird dies der Content. Wenn nicht, ist der Content "".
        String content = "";
        if (messageParameters.length > 1) {
            content = clientMessage.substring(command.length() + 1);
        }
        return content;
    }

    private static String extractMessageCommand(String[] messageParameters) {
        //Erstes Element ist immer ein Parameter (selbst wenn "")
        return messageParameters[0];
    }


}
