// Aufgabe 1.5 (3 Punkte)
public class Transportdrohne extends Drohne {


    private static final int maxEnergie = 150;
    private static final int energieProFeld = 3;
    private static final int ertragTeiler = 2;

    public Transportdrohne(String seriennummer, String name) {
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
        return auftrag.menge() * auftrag.ressource().getWertProEinheit() / ertragTeiler;
    }
}
