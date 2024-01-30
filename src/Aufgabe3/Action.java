package Aufgabe3;

public class Action {
    private String command;
    private String[] content;

    public static Action generateAction(String extractedMessage){
        if (extractedMessage == null || extractedMessage.length() < 2){
            return new Action("", new String[0]);
        }

        String[] messageParams = extractedMessage.split(" ", 2);

        return new Action(messageParams[0], messageParams[1].split(","));
    }

    public Action(String command, String[] content) {
        this.command = command;
        this.content = content;
    }

    public String getCommand() {
        return command;
    }

    public String[] getContent() {
        return content;
    }

}
