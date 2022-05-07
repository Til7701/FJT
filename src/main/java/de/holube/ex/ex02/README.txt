Hier werden alle Klassen aufgelistet, die für die Aufgaben bearbeitet oder erstellt wurden.
Außerdem stehen hier Antworten zu Aufgaben, die Text als Antwort fordern.

Aufgabe 1:
    de.holube.ex.ex02.Logger
    de.holube.ex.ex02.LoggerTest
    de.holube.ex.ex02.StaticLoggerTest

Aufgabe 2:
    de.holube.ex.ex02.NameThreadMain

Aufgabe 3:
    Die Anwendung is außerhalb dieses Projektes beigelegt.

    Ich habe vier Mal Platform.runLater() verwendet. Im PopulationPanel den gesamten Inhalt der Methoden
    paintPopulation() und update(); und bei incrCellSize und decrCellSize den Body der if-Abfrage.

    Alle diese Methoden ändern Elemente, die von JavaFX Application Thread verwaltet werden sollten.
    So, wie es jetzt gemacht ist, muss der Inhalt der Methode paintPopulation() nicht das Platform.runLater() nutzen.
    Allerdings dachte ich, dass das für zukünftige Änderungen zukünftigen Problemen vorbeugt.
    Außerdem müssen aktuell incr- und decrCellSize() das nicht nutzen, da diese nur von JavaFX Application Thread
    aufgerufen werden. Allerdings könnte man ja auf die Idee kommen diese noch von woanders aufzurufen. Daher habe ich
    diese auch geändert.