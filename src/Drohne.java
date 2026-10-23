// Aufgaben 1.3, 1.4 und 1.6 (insgesamt 21 Punkte)
// Die gemeinsame Basis aller Drohnen. Abstrakt: eine "Drohne an sich" gibt es nicht.
public abstract class Drohne {


    // TODO 1.3: Felder - Seriennummer, Name, maximale Energie, aktuelle Energie, Standort.
    private final String seriennummer;
    private final String name;
    private final int maxEnergie;
    private int energie;
    private Koordinate standort;
    // TODO 1.3: Ein static Zaehler, wie viele Drohnen insgesamt gebaut wurden.
    private static int anzahlGebaut = 0;
    // TODO 1.3: Neue Drohne = volle Energie, Standort Basis, Zaehler um 1 erhoeht.
    protected Drohne(String seriennummer, String name, int maxEnergie) {

        this.seriennummer = seriennummer;
        this.name =name;
        this.maxEnergie = maxEnergie;
        this.energie = maxEnergie;
        this.standort = Koordinate.BASIS;

        anzahlGebaut++;
    }

    public static int getAnzahlGebaut() {
        throw new UnsupportedOperationException("TODO 1.3");
    }

    public String getSeriennummer() {
        throw new UnsupportedOperationException("TODO 1.3");
    }

    public String getName() {
        throw new UnsupportedOperationException("TODO 1.3");
    }

    public int getEnergie() {
        throw new UnsupportedOperationException("TODO 1.3");
    }

    public Koordinate getStandort() {
        throw new UnsupportedOperationException("TODO 1.3");
    }

    // Was die Unterklassen selbst festlegen (Aufgabe 1.5):
    protected abstract int berechneBasisBedarf(Auftrag auftrag);

    protected abstract int berechneErtrag(Auftrag auftrag);

    // TODO 1.4: Basisbedarf plus Wetterzuschlag in Prozent, ganzzahlig gerechnet.
    public int energieBedarf(Auftrag auftrag, Wetter wetter) {
        throw new UnsupportedOperationException("TODO 1.4");
    }

    // TODO 1.4: true, wenn der Energiebedarf (mit Wetter) hoechstens so gross ist wie die aktuelle Energie.
    public boolean kannDurchfuehren(Auftrag auftrag, Wetter wetter) {
        throw new UnsupportedOperationException("TODO 1.4");
    }

    // TODO 1.4: Siehe Aufgabenstellung. Schafft sie es nicht: erfolgreich = false, nichts aendert sich.
    public Einsatzergebnis fuehreAus(Auftrag auftrag, Wetter wetter) {
        throw new UnsupportedOperationException("TODO 1.4");
    }

    // TODO 1.3: Energie wieder voll.
    public void laden() {
        throw new UnsupportedOperationException("TODO 1.3");
    }

    // TODO 1.6: equals, hashCode und toString nach Aufgabenstellung.
}
