package Aufgabe3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerTask {

    private DatagramPacket clientMessage;
    private DatagramSocket serverDatagramSocket;

    ServerTask(DatagramPacket clientMessage, DatagramSocket serverDatagramSocket){
        this.clientMessage = clientMessage;
        this.serverDatagramSocket = serverDatagramSocket;
    }

    public DatagramPacket getClientMessage() {
        return clientMessage;
    }

    public DatagramSocket getServerDatagramSocket() {
        return serverDatagramSocket;
    }
}
