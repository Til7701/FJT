Aufgabe 1:
Ja, es kann zu Deadlocks kommen, da die synchronized-Blöcke nicht in der gleichen Reihenfolge ausgeführt werden.
Thread-1 lockt aSync
Thread-2 lockt bSync
Thread-3 lockt cSync
jetzt wartet jeder auf einen anderen, um weiter machen zu können.
In der Klasse Deadlocks ist eine Lösung zu sehen.

Aufgabe 2:
Es kommt zu Fehlverhalten, da in der lock-Methode über das Lock UND über das queueObject synchronisiert wird.
Dadurch wird beim Aufruf der unlock-Methode auf das Lock gewartet. Dieses wird aber niemals freigegeben.

Das Problem kann gelöst werden, indem nur über das Lock synchronisiert wird. Das queueObject wird nur zur Überprüfung
benutzt, ob der aufgeweckte Thread dran ist.
Außerdem muss das notify durch ein notifyAll ersetzt werden, da mehrere Threads warten können.
In der Klasse FairLock ist eine Lösung zu sehen.

Aufgabe 3:
Bei der Klasse NumberSync blockiert setNumber den Aufruf der getNumber Methode.
Bei den beiden anderen Implementierungen der Klasse Number ...