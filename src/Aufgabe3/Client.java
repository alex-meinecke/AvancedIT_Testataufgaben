package Aufgabe3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {

    private static int PORT_SERVER  = 5999;

    private static InetAddress INETADRESS_SERVER;

    static {
        try {
            INETADRESS_SERVER = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("Enter Message :");
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

                String message = userInput.readLine();


                DatagramSocket datagramSocket = new DatagramSocket();

                DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, INETADRESS_SERVER , PORT_SERVER);

                datagramSocket.send(datagramPacket);


                DatagramPacket answerServer = new DatagramPacket(new byte[64000], 64000);
                datagramSocket.receive(answerServer);
                String extractedMessage = new String(answerServer.getData(), 0, answerServer.getLength());
                System.out.println("Waiting for server");

                System.out.println("Server returned: " + extractedMessage);

            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }

}
