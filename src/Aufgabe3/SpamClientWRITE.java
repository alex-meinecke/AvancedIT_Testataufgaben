package Aufgabe3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class SpamClientWRITE {

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
                //System.out.println("Enter Message :");
                //BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));


                int spamCounter = 1;

                while (true) {
                    sleep(2000);
                    String message = "WRITE test,1," + spamCounter;
                    System.out.println("Nr. " + spamCounter + ": Sending " + message);

                    DatagramSocket datagramSocket = new DatagramSocket();

                    DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, INETADRESS_SERVER , PORT_SERVER);

                    datagramSocket.send(datagramPacket);


                    DatagramPacket answerServer = new DatagramPacket(new byte[64000], 64000);
                    System.out.println("Waiting for server");
                    datagramSocket.receive(answerServer);
                    String extractedMessage = new String(answerServer.getData(), 0, answerServer.getLength());

                    System.out.println("Server returned: " + extractedMessage);
                    spamCounter++;
                }


            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }

}
