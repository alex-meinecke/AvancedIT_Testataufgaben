package Aufgabe2;

import java.io.*;
import java.net.Socket;

public class Client {

    // Da Server nur local läuft
    private static final String HOST = "localhost";
    // Socket für Serververbindung
    private static Socket serverSocket;
    // Wie empfohlen, wird mir BufferedReader für Input und PrintWriter für Output gearbeitet
    private static BufferedReader reader;
    private static PrintWriter output;

    public static void main(String[] args) {
        //Der Client wird immer wieder versuchen, sich neu zu Verbinden
        while (true){
            try {
                // Versuch eines Verbindungsaufbaus
                System.out.println("Client is trying to connect server " + HOST + ":" + Server.PORT);
                serverSocket = new Socket(HOST, Server.PORT);

                // Output wird mit dem Serveroutputstream verbunden
                output = new PrintWriter(serverSocket.getOutputStream());

                // Input-Reader wird mit dem Serverinputstream verbunden
                BufferedInputStream inputStream = new BufferedInputStream(serverSocket.getInputStream());
                reader = new BufferedReader(new InputStreamReader(inputStream));

                // Consoleninputstream für Befehlseingaben wird via einen InputStreamReader als BufferedReader lesbar gemacht
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

                // Warten auf eine Consoleneingabe für Weiterverarbeitung
                System.out.println("Please enter your command ...");
                String currentLine = userInput.readLine();

                // Abschicken der Nachricht
                output.println(currentLine);
                output.flush();
                System.out.println("Message " + "\"" + currentLine + "\"" + " submitted");

                // Abwarten und lesen des Ergebnisses
                String response = reader.readLine();

                System.out.println("Server responded: " + response);


            } catch (IOException e) {System.err.println(e);}

        }
    }



}
