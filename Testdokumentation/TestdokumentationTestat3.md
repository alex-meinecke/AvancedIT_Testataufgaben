# Testdokumentation: Testat 2
Autor: Alexander Meinecke
E-Mail: alexandermeinecke@icloud.com

## Disclaimer - Bitte lesen

Die Implementation ist ausführlich kommentiert. Um den Code zu testen, bitte Folgendes beachten:

- Ich schreibe die Nachrichten in einen Ordner auf meinem Desktop (wie in der Vorlesung besprochen). In der MyFile-Klasse findet sich eine statische Variable namens 'PATH'. Bitte geben Sie dort das Verzeichnis an, in das geschrieben und gelesen werden soll.
- Intellij generiert statt "class"-Ordner "out"-Ordner. Ich habe trotzdem ein "class"-Ordner erstellt und die Dateien manuell reinkopiert, um die Verzeichnisse, wie sie in der Aufgabenstellung vorgesehen sind, abzugeben.

## Testfälle

Im Folgen werden alle für mich relevanten Testfälle dargestellt. Die jeweiligen Ausgaben in der Konsole werden ggf. erläutert.

### 1. Testfalle für Kommandos über Client-Textschnittstelle

Folgender Teil der Aufgabenstellung wird getestet:
```
Der Server soll als Worker-Pool-Server auf Port 5999 Aufträge in Form von Strings mit ”READ
filename,line no” entgegennehmen, wobei line no eine positive ganze Zahl sein muss. Daraufhin
wird vom Server die Datei filename geöffnet, die Zeile line no ausgelesen und zuru ̈ckgeschickt.
Außerdem soll der Server auch das Kommando ”WRITE filename,line no,data” verstehen, bei
dem die Zeile line no durch data (kann Kommas und Leerzeichen enthalten) ersetzt werden soll.
Falls sich im Basisverzeichnis des Servers keine solche Datei befindet oder keine entsprechende Zeile
vorhanden ist, soll an den Client eine Fehlermeldung zuru ̈ckgesendet werden.
```
Anmerkung: Die MyFile-Klasse kann zusätzlich neue Dateien erzeugen. Beim Schreiben können notfalls weitere Zeilen hinzugefügt werden.

#### 1.1 Testfall - WRITE-Kommando

Client:
```
Enter Message :
WRITE test,1,Ich bin Zeile 1 :)
Waiting for server
Server returned: New line content: Ich bin Zeile 1 :)
Enter Message :

```

#### 1.2 Testfall - READ-Kommando

Client:
```
Enter Message :
READ test,1
Waiting for server
Server returned: Ich bin Zeile 1 :)
```



#### 1.3 Testfall - Negative Zeile

Client: 
```
READ test,-1
Waiting for server
Server returned: Line not found
```

#### 1.4 Testfall - PATH ist falsch / Datei existiert nicht (nur für Reader)

Client:
```
READ file_der_nicht_extistiert,1
Waiting for server
Server returned: File /Users/I569702/Documents/AdvancedIT/Advanced_IT_Testataufgaben/file_der_nicht_extistiert not found
```

#### 1.5 Testfall - Falsches Format

Client:
```
Enter Message :
WRITE test;neuer inhalt;1
Waiting for server
Server returned: Line index not found
Enter Message :
READ test 1
Waiting for server
Server returned: File /Users/I569702/Documents/AdvancedIT/Advanced_IT_Testataufgaben/test 1 not found
```

#### 1.6 Testfall - Ungültiges Kommando

Client:
```
Enter Message :
PIZZA test
Waiting for server
Server returned: Command not defined.
```


### Testfalle für zweites Leser-Schreiber-Problem

### Testfälle für Erzeuger-Verbraucher-Problem




