// Aufgabe 1.5 (3 Punkte)
public class Aufklaerungsdrohne extends Drohne {

    private static final int maxEnergie = 60;
    private static final int energieProFeld = 1;
    private static final int ertrag = 0;

    public Aufklaerungsdrohne(String seriennummer, String name) {
        // TODO 1.5: maximale Energie laut Aufgabenstellung (als Konstante, keine "Zahl im Code")
        super(seriennummer, name, maxEnergie);
    }

    @Override
    protected int berechneBasisBedarf(Auftrag auftrag) {
        int abstand = getStandort().abstandZu(auftrag.ziel());
        return abstand * energieProFeld;
    }

    @Override
    protected int berechneErtrag(Auftrag auftrag) {
        return ertrag;
    }
}
