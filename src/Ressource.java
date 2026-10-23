// Aufgabe 1.1 (2 Punkte)
// Jede Ressource hat einen Wert pro Einheit: EIS = 2, ERZ = 5, SILIZIUM = 9.
// Ergaenze das, was dafuer noetig ist (Feld, Konstruktor, Werte bei den Konstanten).
public enum Ressource {
    EIS(2), ERZ(5), SILIZIUM(9);

    private final int wertProEinheit;

    private Ressource(int wertProEinheit) {
        this.wertProEinheit = wertProEinheit;
    }
    public int getWertProEinheit() {
        return wertProEinheit;
    }

}
