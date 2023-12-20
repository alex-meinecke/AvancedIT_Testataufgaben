# Testdokumentation: Testat 1 
Autor: Alexander Meinecke
Email: alexandermeinecke@icloud.com

# Disclaimer
Die Implementation ist ausführlich kommentiert.
Um die eigentlichen Besonderheiten der jeweiligen Implementierungen hervorzuheben,
wurde eine abstrakte Klasse 'TrainDraftForBothExercises' angelegt, in der die in der Aufgabenstellung beschriebenen Eigenschaften für Lok0 und Lok1 auszulagern. 
Die jeweiligen Lösungen für a) und b) befinden in den gleichnamigen Paketen.

# Aufgabe 1a

Alle Testfälle finden sich zum Nachprüfen auskommentiert in der TrackMangerCreatorConsumerProblem-Klasse im Paket a. Um Anhanltspunkte in der Konsolenausgabe zu ermöglichen, gibt es Referenzpunkte, die mit "[Zahl]" gekennzeichnet sind.

## Testfall 1

Im ersten Testfall sind beide Loks gleich schnell. Hier sollte Lok 0 beim gleichzeitigen Erreichen der Weiche immer bevorzugt werden. Ausnahme ist, wenn die Strecke bereits benutzt wird.

``` java
    train0.setTimeToPassPrivateTrainTrackInSeconds(2);
    train1.setTimeToPassPrivateTrainTrackInSeconds(2);   
```

Nach Ausführung von der main-Methode mit dem auskommentierten Testfall von TrackMangerCreatorConsumerProblem ergibt sich in der Konsole folgende Ausgabe:

```
[Start]
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	(50%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	(50%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 2s/2s 	(100%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 2s/2s 	(100%)
Lok 1 prüft, ob sie überhaupt weiterfahren darf.
Lok 0 prüft, ob sie überhaupt weiterfahren darf.
Lok 0 fragt kritischen Streckenabschnitt an.
Lok 0 hat kritischen Streckenabschnitt reserviert.
Lok 0 fährt innerhalb des kritischen Abschnittes
Lok 0 gibt anderen Zug eine Fahrtberechtigung
Lok 1 fragt kritischen Streckenabschnitt an.
Lok 0 hat kritischen Streckenabschnitt freigegeben.
Lok 1 hat kritischen Streckenabschnitt reserviert.
Lok 1 fährt innerhalb des kritischen Abschnittes
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	(50%)
Lok 1 gibt anderen Zug eine Fahrtberechtigung
Lok 1 hat kritischen Streckenabschnitt freigegeben.
[...]
```
In diesem Ausschnitt der Konsole kann nachvollzogen werden, dass Lok0 und Lok1 anfangs gleichzeitig und unabhängig ihre eigene Streckeabfahren.
Sobald sie fertig sind, prüft Lok1 (zufällig als erste) über ihre Durchfahrtsberechtigungen via den 'availablePassesForTrain' ihre Zugriffsberechtigung. 
Da aber Lok0 die erste Durchfahrtsberechtigung hat und nicht Lok1, muss diese auf den erfolgreichen Drucklauf von Lok0 warten. Deshalb gibt Lok1 nach der Durchfahrtsberechtigungenprüfung in der 5. Zeile nichts mehr aus.
Direkt danach prüft Lok0 ihre Durchfahrtsberechtigung über ihr 'availablePassesForTrain', bekommt sie und fährt im kritischen Abschnitt. Beim Verlassen gibt Lok0 Lok1 eine Durchfahrtsberechtigung. Diese (Lok1) löst die sofort ein und muss jetzt nur noch auf die freigabe der Strecke warten, die aktuell offiziell noch von der anderen Lok in Verwendung ist.
Unmittelbar danach gibt Lok0 die Strecke frei und Lok1 kann fahren.

## Testfall 2
Im zweiten Testfall ist Lok0 beim Zurücklegen der eigenen Strecke schneller. Dies soll zeigen, dass Lok0 immer vor Lok1 kommt, aber dass sich diese immer noch abwechseln müssen.

``` java
    train0.setTimeToPassPrivateTrainTrackInSeconds(2);
    train1.setTimeToPassPrivateTrainTrackInSeconds(20);
```
Nach Ausführung von der main-Methode mit dem auskommentierten Testfall von TrackMangerCreatorConsumerProblem ergibt sich in der Konsole folgende Ausgabe:

```
[Start]
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	    (50%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 1s/20s 	    (5%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 2s/2s 	    (100%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 2s/20s 	    (10%)
Lok 0 prüft, ob sie überhaupt weiterfahren darf.
Lok 0 fragt kritischen Streckenabschnitt an.
Lok 0 hat kritischen Streckenabschnitt reserviert.
Lok 0 fährt innerhalb des kritischen Abschnittes
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 3s/20s 	    (15%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 4s/20s 	    (20%)
Lok 0 gibt anderen Zug eine Fahrtberechtigung
Lok 0 hat kritischen Streckenabschnitt freigegeben.
[1]
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 5s/20s 	    (25%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	    (50%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 6s/20s 	    (30%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 2s/2s 	    (100%)
[2]
Lok 0 prüft, ob sie überhaupt weiterfahren darf.
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 7s/20s 	    (35%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 8s/20s 	    (40%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 9s/20s 	    (45%)
[Lok1 zahlt weiter hoch]
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 19s/20s 	    (95%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 20s/20s 	    (100%)
Lok 1 prüft, ob sie überhaupt weiterfahren darf.
Lok 1 fragt kritischen Streckenabschnitt an.
Lok 1 hat kritischen Streckenabschnitt reserviert.
Lok 1 fährt innerhalb des kritischen Abschnittes
Lok 1 gibt anderen Zug eine Fahrtberechtigung
Lok 1 hat kritischen Streckenabschnitt freigegeben.
[3]
Lok 0 fragt kritischen Streckenabschnitt an.
Lok 0 hat kritischen Streckenabschnitt reserviert.
Lok 0 fährt innerhalb des kritischen Abschnittes
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 1s/20s 	    (5%)
Lok 0 gibt anderen Zug eine Fahrtberechtigung
Lok 0 hat kritischen Streckenabschnitt freigegeben.
[...]
```
In der Ausgabe hat Lok0, wie zu erwarten, aufgrund ihrer verhältnismäßig kurzen Laufzeit ihren ersten erfolgreichen Durchlauf bereits abgeschlossen, während Lok1 noch auf ihrer eigenen Strecke fährt (siehe [1]).
Nachdem sie dann wieder schnell ihre Strecke durchlaufen hat und Lok1 noch auf ihrer privaten Strecke ist, will sie nochmal den kritischen Abschnitt durchfahren. Sie überprüft ihre Durchfahrtsberechtigung, aber muss warten, da Lok1 ihr noch nicht die nächste gegeben hat.
Das sieht man daran, dass nach der Überprüfung von Lok0 keine Meldung in der Konsole kommt, während Lok1 weiter ihre eigene Streck durchfährt (siehe [2]).
Nachdem Lok1 am kritischen Abschnitt angekommen ist, ihn durchfahren ist und Lok0 wieder die Durchfahrtsbrechtigung erteilt hat, sperrt Lok0 wieder den kritischen Abschnitt und setzt ihre Fahrt fort (siehe [3]). 

## Testfall 3
Im dritten Testfall soll das Gegenteil überprüft werden. Lok1 soll nun viel schneller als Lok0 sein. Hier soll gezeigt werden, dass - obwohl Lok1 schneller an der Weiche ist - Lok0 trotzdem die erste Durchfahrtsberechtigung hat (obwohl dies schon zufällig im Testfall 1 bewiesen wurde).

``` java
    train0.setTimeToPassPrivateTrainTrackInSeconds(2);
    train1.setTimeToPassPrivateTrainTrackInSeconds(10);
```
Nach Ausführung von der main-Methode mit dem auskommentierten Testfall von TrackMangerCreatorConsumerProblem ergibt sich in der Konsole folgende Ausgabe:

``` java
[Start]
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	    (50%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 2s/10s 	    (20%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 2s/2s 	    (100%)
[1]
Lok 1 prüft, ob sie überhaupt weiterfahren darf.
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 3s/10s 	    (30%)
[Lok 0 fährt restliche private Strecke ab]
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 9s/10s 	    (90%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 10s/10s 	    (100%)
Lok 0 prüft, ob sie überhaupt weiterfahren darf.
Lok 0 fragt kritischen Streckenabschnitt an.
Lok 0 hat kritischen Streckenabschnitt reserviert.
Lok 0 fährt innerhalb des kritischen Abschnittes
Lok 0 gibt anderen Zug eine Fahrtberechtigung
[2]
Lok 1 fragt kritischen Streckenabschnitt an.
Lok 0 hat kritischen Streckenabschnitt freigegeben.
Lok 1 hat kritischen Streckenabschnitt reserviert.
[3]
Lok 1 fährt innerhalb des kritischen Abschnittes
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 1s/10s 	    (10%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 2s/10s 	    (20%)
[...]
```

Nach dem Start ist Lok1 wie zu erwarten als erste an der Weiche. Sie darf trotzdem nicht fahren, da sie keine Durchfahrtsberechtigung hat. Aus diesem Grund stoppt die Ausgabe von Lok1 nach dieser Berechtigungsprüfung (siehe [1]).
Erst nachdem Lok0 einmal Lok1 die nächste Durchfahrtsberechtigung gegeben hat (siehe [2]) und auch den kritischen Abschnitt freigegeben hat, kann Lok1 ihre Fahrt in den kritischen Abschnitt starten (siehe [3]).

# Aufgabe 1b
Alle Testfälle finden sich zum Nachprüfen auskommentiert in der TrackManagerForPrivateSemaphoreSolution-Klasse im Paket b. Da in dieser Lösung sehr viel über das Verhalten der Threads bzw. Loks in der Ausgabe im Konsole nachzuvollziehen ist, wird in 1b die Ausgabe für die einzelnen Testfälle nicht so ausführlich wie für Aufgabe 1a erläutert.

## Testfall 1

Im ersten Testfall sind beide Loks wieder gleich schnell. Hier sollte Lok 0 beim gleichzeitigen Erreichen der Weiche immer bevorzugt werden. Ausnahme ist, wenn die Strecke bereits benutzt wird.

``` java 
    trains[0].setTimeToPassPrivateTrainTrackInSeconds(2);
    trains[1].setTimeToPassPrivateTrainTrackInSeconds(2);
```

Nach Ausführung von der main-Methode von TrackManagerForPrivateSemaphoreSolution mit dem auskommentierten Testfall ergibt sich in der Konsole folgende Ausgabe:

```
[Start]
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	(50%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	(50%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 2s/2s 	(100%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 2s/2s 	(100%)
Lok 1 fragt Zugriff auf geteilte Daten an.
Lok 0 fragt Zugriff auf geteilte Daten an.
Lok 1 prüft ob der geteilte Streckenabschnitt frei ist und ob sie erstmal Lok 0 vorlassen muss.
Lok 1 darf nicht fahren und wartet nun.
Lok 1 gibt Zugriff auf geteilte Daten wieder frei.
Lok 0 prüft ob der geteilte Streckenabschnitt frei ist und ob sie erstmal Lok 1 vorlassen muss.
Lok 0 darf fahren und gibt ihren privaten Semaphore eine Durchfahrtsbrechtigung.
Lok 0 gibt Zugriff auf geteilte Daten wieder frei.
Lok 0 fährt innerhalb des kritischen Abschnittes
Lok 0 hat nach erfolgreicher Durchfahrt ihren Durchfahrtszähler auf 1 erhöht.
Lok 0 fragt Zugriff auf geteilte Daten an.
Lok 0 prüft ob Lok 1 wartet
Lok 0 schickt Lok 1 das Signal zur weiterfahrt
Lok 0 gibt Zugriff auf geteilte Daten wieder frei.
Lok 1 fährt innerhalb des kritischen Abschnittes
[...]
```

Lok1 muss so lange Warten, bis Lok0 das erste Mal durchgefahren ist und Lok 1 das Signal zur weiterfahrt schickt.

## Testfall 2

Im zweiten Testfall ist Lok0 wieder beim Zurücklegen der eigenen Strecke schneller. Dies soll zeigen, dass Lok0 immer vor Lok1 kommt, aber dass sich diese immer noch abwechseln müssen.

``` java
trains[0].setTimeToPassPrivateTrainTrackInSeconds(2);
trains[1].setTimeToPassPrivateTrainTrackInSeconds(10);
```
Nach Ausführung von der main-Methode von TrackManagerForPrivateSemaphoreSolution mit dem auskommentierten Testfall ergibt sich in der Konsole folgende Ausgabe:

```
[Start]

Lok 1 fährt private Strecke ab. Verbleibende Dauer: 1s/10s 	(10%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	(50%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 2s/10s 	(20%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 2s/2s 	(100%)
Lok 0 fragt Zugriff auf geteilte Daten an.
Lok 0 prüft ob der geteilte Streckenabschnitt frei ist und ob sie erstmal Lok 1 vorlassen muss.
Lok 0 darf fahren und gibt ihren privaten Semaphore eine Durchfahrtsbrechtigung.
Lok 0 gibt Zugriff auf geteilte Daten wieder frei.
Lok 0 fährt innerhalb des kritischen Abschnittes
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 3s/10s 	(30%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 4s/10s 	(40%)
Lok 0 hat nach erfolgreicher Durchfahrt ihren Durchfahrtszähler auf 1 erhöht.
Lok 0 fragt Zugriff auf geteilte Daten an.
Lok 0 prüft ob Lok 1 wartet
Lok 0 gibt die geteilte Strecke wieder frei, da Lok 1 nicht wartet
Lok 0 gibt Zugriff auf geteilte Daten wieder frei.
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 5s/10s 	(50%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 1s/2s 	(50%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 6s/10s 	(60%)
Lok 0 fährt private Strecke ab. Verbleibende Dauer: 2s/2s 	(100%)
Lok 0 fragt Zugriff auf geteilte Daten an.
[1]
Lok 0 prüft ob der geteilte Streckenabschnitt frei ist und ob sie erstmal Lok 1 vorlassen muss.
Lok 0 darf nicht fahren und wartet nun.
Lok 0 gibt Zugriff auf geteilte Daten wieder frei.
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 7s/10s 	(70%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 8s/10s 	(80%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 9s/10s 	(90%)
Lok 1 fährt private Strecke ab. Verbleibende Dauer: 10s/10s 	(100%)
Lok 1 fragt Zugriff auf geteilte Daten an.
Lok 1 prüft ob der geteilte Streckenabschnitt frei ist und ob sie erstmal Lok 0 vorlassen muss.
Lok 1darf fahren und gibt ihren privaten Semaphore eine Durchfahrtsbrechtigung.
Lok 1 gibt Zugriff auf geteilte Daten wieder frei.
Lok 1 fährt innerhalb des kritischen Abschnittes
Lok 1 hat nach erfolgreicher Durchfahrt ihren Durchfahrtszähler auf 1 erhöht.
Lok 1 fragt Zugriff auf geteilte Daten an.
Lok 1 prüft ob Lok 0 wartet
[2]
Lok 1 schickt Lok 0 das Signal zur weiterfahrt
Lok 0 fährt innerhalb des kritischen Abschnittes
```
Nachdem Lok 0 schon einmal gefahren ist und nochmal die Druchfahrt durch den geteilten Abschnitt anfragt, muss muss diese erstmal warten (siehe [1]).
Erst wenn Lok 1 durchgefahren ist, darf Lok 0 weiterfahren (siehe [2]).

## Testfall 3

Im dritten Testfall soll wieder das Gegenteil überprüft werden. Lok1 soll nun viel schneller als Lok0 sein. Hier soll gezeigt werden, dass - obwohl Lok1 schneller an der Weiche ist - Lok0 trotzdem die erste Durchfahrtsberechtigung hat (obwohl dies auch hier wieder schon zufällig im Testfall 1 bewiesen wurde).





