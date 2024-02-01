package Aufgabe3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class SpamClientRWRW {

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

                //System.out.println("Enter Message :");
                //BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));


                int spamCounter = 1;

                String writeMessage = "WRITE test,1,1";
                String readMessage = "READ test,1";

                String[] messages = {readMessage, writeMessage, readMessage, writeMessage};

                for (int i = 0; i < messages.length; i++) {
                    sleep(1000);

                    System.out.println("Nr. " + spamCounter + ": Sending " + messages[i]);

                    DatagramSocket datagramSocket = new DatagramSocket();

                    DatagramPacket datagramPacket = new DatagramPacket(messages[i].getBytes(), messages[i].getBytes().length, INETADRESS_SERVER , PORT_SERVER);

                    datagramSocket.send(datagramPacket);

                    /*
                    DatagramPacket answerServer = new DatagramPacket(new byte[64000], 64000);
                    System.out.println("Waiting for server");
                    datagramSocket.receive(answerServer);
                    String extractedMessage = new String(answerServer.getData(), 0, answerServer.getLength());

                    System.out.println("Server returned: " + extractedMessage);

                     */
                    spamCounter++;
                }




        } catch (Exception e) {
            System.err.println(e);
        }

    }

}
