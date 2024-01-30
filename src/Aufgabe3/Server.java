package Aufgabe3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
    // Vorgegebener Port
    private static int PORT = 5999;
    // Generierung eines Dispatchers
    private static Dispatcher dispatcher = new Dispatcher();
    // DatagramSocket für den Server
    private static DatagramSocket datagramSocket;

    public static void main(String[] args) {
        try {
            // Versuch den UDP Socket zu definieren (try)
            datagramSocket = new DatagramSocket(PORT);
            System.out.println("File server ready...");

            // Ständig neue Nachrichten empfangen (while true)
        while (true) {
            try {
                // DatagramPacket für die zu erwatende Clientnachricht vorbereiten
                DatagramPacket message = new DatagramPacket(new byte[64000], new byte[64000].length);
                // Auf Nachricht von einem Client warten
                datagramSocket.receive(message);

                //Nachricht direkt an den Dispatcher weitergeben
                System.out.println("Added new ServerTask to dispatcher");
                dispatcher.addNewServerTask(message);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }



}
