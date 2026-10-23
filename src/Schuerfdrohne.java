// Aufgabe 1.5 (3 Punkte)
public class Schuerfdrohne extends Drohne {

    private static final int maxEnergie = 100;
    private static final int energieProFeld = 2;

    public Schuerfdrohne(String seriennummer, String name) {
        // TODO 1.5: maximale Energie laut Aufgabenstellung (als Konstante, keine "Zahl im Code")
        super(seriennummer, name, maxEnergie);
    }

    @Override
    protected int berechneBasisBedarf(Auftrag auftrag) {

        int abstand = getStandort().abstandZu(auftrag.ziel());

        return abstand * energieProFeld + auftrag.menge();
    }

    @Override
    protected int berechneErtrag(Auftrag auftrag) {
        return auftrag.menge() * auftrag.ressource().getWertProEinheit();
    }
}
