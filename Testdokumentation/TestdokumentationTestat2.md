# Testdokumentation: Testat 2
Autor: Alexander Meinecke
Email: alexandermeinecke@icloud.com

## Disclaimer - Bitte lesen

Die Implementation ist ausführlich kommentiert. Um den Code zu testen, bitte Folgendes beachten:

- Ich schreibe die Nachrichten in einen Ordner auf meinem Desktop (wie in der Vorlesung besprochen). In der Serverklasse findet sich eine statische Variable namens 'PATH_FOR_MESSAGE_FILE'. Bitte geben Sie dort das Verzeichnis an, in das geschrieben werden soll. Sollte das nicht passieren, gibt es aber in der Console noch einen Hinweis. Alternativ können Sie, wenn sie nur den Bytecode ausführen wollen, den path als Parameter beim Starten übergeben. Hier ein Beispiel:  ```java Aufgabe2.Server /Users/I569702/Desktop/message_folder2/```
- Intellij generiert statt "class"-Ordner "out"-Ordner. Ich habe trotzdem ein "class"-Ordner erstellt und die Dateien manuell reinkopiert, um die Verzeichnisse, wie sie in der Aufgabenstellung vorgesehen sind, abzugeben.

## Testfälle

Im Folgen werden alle für mich relevanten Testfälle dargestellt. Es wird jeweils die Server- und Clientsicht berücksichtigt. Die jeweiligen Ausgaben in der Console werden ggf. erläutert.

### Testfall 1 - SAVE

Der folgende Testfall soll folgendes zeigen:
- SAVE funktioniert wie gewollt
- Es handelt sich um einen non-persistent Server


Von der Clientseite folgendermaßen:
```
Please enter your command ...
SAVE Ich bin eine Testnachricht :)
Client is trying to connect server localhost:7777
Message "SAVE Ich bin eine Testnachricht :)" submitted
Server responded: KEY 995349111
Please enter your command ...
```
Nachdem sich der Client verbunden hat, darf er einen Befehl eingeben. Nachdem er "SAVE Ich bin eine Testnachricht :)" übergeben hat und der Server mit dem Key geantwortet hat, versucht er sich wieder mit dem Server zu verbinden, schafft es und erwartet die nächste Anfrage. Dies deutet darauf hin, dass der Server non-persistent ist, da er die Verbindung abgebrochen hat, nach dem er das Ergebnis übermittelt hat.  

Von der Serverseite sieht das folgendermaßen aus:
```
Waiting for client
Client connected
Waiting for message from client
Message form client: SAVE Ich bin eine Testnachricht :)
File 995349111 generated with following content: Ich bin eine Testnachricht :)
Disconnecting current client
Client disconnected
====================================
Waiting for client
Client connected
Waiting for message from client
```
Dies bestätigt nochmal: Die Datei für die Nachricht wurde erfolgreich gespeichert und der Schlüssel zurückgegeben.
Anschließend wurde die Verbindung aufgehoben und direkt eine neue zugelassen.

### Testfall 2 - GET

Der folgende Testfall soll folgendes zeigen:
- GET FUNKTIONALITÄT

Von der Clientseite folgendermaßen:
```
Please enter your command ...
GET 995349111
Client is trying to connect server localhost:7777
Message "GET 995349111" submitted
Server responded: OK Ich bin eine Testnachricht :)
Please enter your command ...
```
Die Antwort wie erwartet: OK + die Nachricht.

Von der Serverseite folgendermaßen:
```
Starting Server
Waiting for client
Client connected
Waiting for message from client
Message form client: GET 995349111
Message 995349111 loaded: Ich bin eine Testnachricht :)
Disconnecting current client
Client disconnected
```

### Testfall 3 - Abruf mit einem flaschen Schlüssel

Der folgende Testfall soll folgendes zeigen:
- FAILED

Von der Clientseite folgendermaßen:
```
Please enter your command ...
GET 123456
Client is trying to connect server localhost:7777
Message "GET 123456" submitted
Server responded: FAILED
Please enter your command ...
```
Antwort ist FAILED.

Von der Serverseite folgendermaßen:
```
Starting Server
Waiting for client
Client connected
Waiting for message from client
Message form client: GET 123456
Error while reading file.
Disconnecting current client
Client disconnected
```

### Testfall 4 - Client bricht ab.

Der folgende Testfall soll folgendes zeigen:
- Server merkt, wenn TCP-Verbindung mit Client nicht mehr besteht und gibt Verbindung wieder frei.

Von der Clientseite folgendermaßen (Ausfall "simulieren"):
```
Please enter your command ...
SAVE Eine Nachricht
Client is trying to connect server localhost:7777

Process finished with exit code 130 (interrupted by signal 2:SIGINT)
```

Von der Serverseite folgendermaßen:
```
Waiting for client
Client connected
Waiting for message from client
Message form client: null
Correction of the client message: null is now a parameter without chars
Disconnecting current client
Client disconnected
====================================
Waiting for client
```
Die erwartete Antwort vom Client wird nie erfüllt (deshalb null). Am Schluss wartet der Server auf einen neuen CLient.

### Testfall 5 - Falsches Eingabekommando

Der folgende Testfall soll folgendes zeigen:
- Client gibt Kommando, dass der Server nicht kennt

Dies war nicht umbedingt in der Aufgabe vorausgesetzt, aber meiner Meinung nach wäre das laut Definition in der Aufgabenstellung nicht FAILED, da sich dies nur auf das Gelingen der auf der Folie definierten Operationen bezieht.

Von der Clientseite folgendermaßen:
```
Please enter your command ...
back mir eine Pizza
Client is trying to connect server localhost:7777
Message "back mir eine Pizza" submitted
Server responded: Command "back" unknown
Please enter your command ...
```
Von der Serverseite folgendermaßen:
```
Waiting for client
Client connected
Waiting for message from client
Message form client: back mir eine Pizza
Command "back" unknown
Disconnecting current client
Client disconnected
```
### Testfall 6 - Ordnerverzeichnis existiert nicht

Der folgende Testfall soll folgendes zeigen:
- Von der Serverseite wird erkannt und gewarnt, dass die Speicherung nicht funktioniert
- Laut Definition auf der Folie wird an den Client nur FAILED geschickt.

Von der Clientseite folgendermaßen:
```
Please enter your command ...
SAVE NACHRICHT
Client is trying to connect server localhost:7777
Message "SAVE NACHRICHT" submitted
Server responded: FAILED
Please enter your command ...
```

Von der Serverseite:
```
Waiting for client
Client connected
Waiting for message from client
Message form client: SAVE NACHRICHT
java.io.FileNotFoundException: /Users/I569702/Desktop/message_folder/956703768 (No such file or directory)
Error while generating file. Please check if PATH (/Users/I569702/Desktop/message_folder/) exists or access permission is granted
Disconnecting current client
Client disconnected
```

### Testfall 7 - Mehr als ein Client wollen sich verbinden

Der folgende Testfall soll folgendes zeigen:
- Client blockiert Server so kurz wie möglich (erst nach Befehlseingabe)

Von der Clientseite (1) folgendermaßen:
```
Please enter your command ...
SAVE Nachricht von Client 1 
Client is trying to connect server localhost:7777
Message "SAVE Nachricht von Client 1 " submitted
Server responded: KEY 261156229
```

Von der Clientseite (2) folgendermaßen:
```
Please enter your command ...
SAVE Nachricht von Client 2
Client is trying to connect server localhost:7777
Message "SAVE Nachricht von Client 2" submitted
Server responded: KEY 141671968
```

Von der Serverseite:
````
Waiting for client
Client connected
Waiting for message from client
Message form client: SAVE Nachricht von Client 2
File 141671968 generated with following content: Nachricht von Client 2
Disconnecting current client
Client disconnected
====================================
Waiting for client
Client connected
Waiting for message from client
Message form client: SAVE Nachricht von Client 1 
File 261156229 generated with following content: Nachricht von Client 1 
Disconnecting current client
Client disconnected
====================================
Waiting for client
````

