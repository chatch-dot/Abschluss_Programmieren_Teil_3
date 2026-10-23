# Operation Roter Staub

Die Aufgabenstellung steht im Teams-Chat / im Aufgaben-Dokument von Dave.
Dieser Ordner ist dein Startpaket.

```
Roter_Staub/
  lib/    die JUnit-Jar
  src/    dein Code. Hier arbeitest du. Ueberall, wo TODO steht, fehlt etwas.
  test/   die Tests (nicht aendern) und TestMain (die Test-Runtime)
          EigeneTests.java ist fuer deine eigenen Tests (Aufgabe 3.3)
  REFLEXION.md   am Ende ausfuellen
```

## Einrichten in IntelliJ (ca. 10 Minuten)

1. Ordner in ein eigenes neues Repository legen und in IntelliJ oeffnen.
2. Rechtsklick auf `src` > Mark Directory as > Sources Root.
3. Rechtsklick auf `test` > Mark Directory as > Test Sources Root.
4. File > Project Structure > Libraries > `+` > Java > die Jar aus `lib` waehlen > OK.
5. `test/TestMain.java` oeffnen, gruenen Pfeil neben `main` klicken.

Am Anfang ist fast alles ROT. Das ist normal. Unten im Run-Fenster steht pro Test eine Zeile:

```
  OK      EnumsTest: Wetter ist ein enum mit KLAR, STAUB, STURM (in dieser Reihenfolge)
  FEHLER  EnumsTest: Wetter: Zuschlag KLAR 0, STAUB 25, STURM 100 Prozent
          TODO 1.1
          bei Wetter.java:7

ROT: 73 Tests fehlgeschlagen, 5 erfolgreich.
```

Die Zeile "bei Datei:Zeile" zeigt, wo der Test stolpert. "TODO 1.2" heisst: Dort ist noch der Platzhalter.

## Nur eine Testklasse laufen lassen

Run > Edit Configurations > TestMain > Program arguments: `DrohneTest`
(mehrere mit Leerzeichen: `DrohneTest KoordinateTest`). Feld leeren = wieder alle.

## Reihenfolge-Tipp

Stufe 1 zuerst: Enums und Koordinate (Tests `EnumsTest`, `KoordinateTest`), dann Drohne (`DrohneTest`), dann die drei Arten (`DrohnenArtenTest`). Erst wenn die gruen sind, lohnt sich Stufe 2.

Der Ordner `out/` wird nicht eingecheckt (steht in `.gitignore`).
