package Aufgabe3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {
    // Vorgegebener Port
    private static int PORT_SERVER  = 5999;

    // Adresse
    private static InetAddress INETADRESS_SERVER;

    static {
        try {
            // Auf den localhost setzten
            INETADRESS_SERVER = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            //Ständige Wiederholung
            while (true) {
                System.out.println("Enter Message :");
                // Verarbeitung des Konsoleninputs
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                String message = userInput.readLine();

                DatagramSocket datagramSocket = new DatagramSocket();

                // Vorbereitung und Sendung des Nachrichtenpaktes an den Server
                DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, INETADRESS_SERVER , PORT_SERVER);
                datagramSocket.send(datagramPacket);

                // Vorbereitung und warten auf den Empfangen des Antwortpackets
                DatagramPacket answerServer = new DatagramPacket(new byte[64000], 64000);
                datagramSocket.receive(answerServer);

                // Verarbeitung und Ausgabe der Antwortnachricht
                String extractedMessage = new String(answerServer.getData(), 0, answerServer.getLength());
                System.out.println("Waiting for server");

                System.out.println("Server returned: " + extractedMessage);
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }

}
