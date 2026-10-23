// Aufgabe 1.5 (3 Punkte)
public class Schuerfdrohne extends Drohne {

    public Schuerfdrohne(String seriennummer, String name) {
        // TODO 1.5: maximale Energie laut Aufgabenstellung (als Konstante, keine "Zahl im Code")
        super(seriennummer, name, 0);
    }

    @Override
    protected int berechneBasisBedarf(Auftrag auftrag) {
        throw new UnsupportedOperationException("TODO 1.5");
    }

    @Override
    protected int berechneErtrag(Auftrag auftrag) {
        throw new UnsupportedOperationException("TODO 1.5");
    }
}
