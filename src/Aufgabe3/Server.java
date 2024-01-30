package Aufgabe3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {

    private static int PORT = 5999;

    private static Dispatcher dispatcher = new Dispatcher();

    private static DatagramSocket datagramSocket;

    public static void main(String[] args) {
        try {
            datagramSocket = new DatagramSocket(PORT);
            System.out.println("File server ready...");
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                DatagramPacket message = new DatagramPacket(new byte[64000], new byte[64000].length);

                datagramSocket.receive(message);
                System.out.println("Added new ServerTask to dispatcher");
                dispatcher.addServerTask(message, datagramSocket);

            } catch (SocketException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }



}
