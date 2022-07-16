Aufgabe 1: Prim

Aufgabe 2:
Main: LineCounterMain
Einfache Version: LineCounterSimple
Komplexe Version: LineCounter

Probleme bei relativ großen Ordnern (Bei den Tests waren in dem Ordner 16 kleine Java-Projekte):
 - Wenn beide Streams parallel sind, hören Aufrufe der countOccurrence-Methode nach einiger Zeit auf. Die CPU-Auslastung
 ist bei 100 %.
 - Wenn nur die Zeilen parallel bearbeitet werden, stürzt die VM ab. Siehe logs.
    + Memory-Leak?
