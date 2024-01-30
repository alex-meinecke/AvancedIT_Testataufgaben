package Aufgabe3;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Action {
    // Command wie "READ" oder "WRITE"
    private String command;
    // Alles nach Read durch "," separiert (z.B. Dateiname und Zeilennummer)
    private String[] content;
    // Addresse des Empfängers
    private InetAddress clientAddress;
    // Port des Empfängers
    private int clientPort;


    // Generiert aus einer Clientnachricht als DatagramPacket eine Action
    public static Action generateAction(DatagramPacket clientMessage){
        // Nachricht wird extrahiert
        String extractedMessage = new String(clientMessage.getData(), 0, clientMessage.getLength());

        // Wenn die Nachricht leer ist
        if (extractedMessage.isEmpty()){
            String[] emptyContent = {"", ""};
            return new Action("", emptyContent , clientMessage.getAddress(), clientMessage.getPort());
        }

        //Teilt Command vom Content z.B. "GET test,1,hallo"
        String[] messageParams = extractedMessage.split(" ", 2);

        try {
            // Versuch, den Content aufzuteilen (z.B. WRITE test,1,hallo => Content = [test,1,hallo])
            // Rückgabe der Action
            return new Action(messageParams[0],  messageParams[1].split(","), clientMessage.getAddress(), clientMessage.getPort());
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println(e);
            // Im Falle, dass der Content nicht richtig gesetzt wird bzw. die Kommata fehlen, wird ein Dummy-Stringarray erzeugt
            // (Behandlung erfolgt später auf anderer Ebene)
            String[] emptyMessageContent = {"", "", ""};
            // Rückgabe der Action
            return new Action(messageParams[0],  emptyMessageContent, clientMessage.getAddress(), clientMessage.getPort());
        }

    }

    // Constructor der Action
    public Action(String command, String[] content, InetAddress clientAddress, int clientPort) {
        this.command = command;
        this.content = content;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
    }

    // Getter
    public String getCommand() {
        return command;
    }

    public String[] getContent() {
        return content;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public int getClientPort() {
        return clientPort;
    }
}
