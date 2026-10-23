// Aufgabe 1.2 (6 Punkte)
// Ein Punkt auf dem Marsplan. Als record bereits angelegt.
public record Koordinate(int x, int y) {

    // TODO 1.2: BASIS soll die Koordinate 0/0 sein (eine Konstante, die es nur einmal gibt).
    public static final Koordinate BASIS = null;

    // TODO 1.2: Abstand = Schritte in x plus Schritte in y (nie negativ).
    public int abstandZu(Koordinate andere) {
        throw new UnsupportedOperationException("TODO 1.2");
    }
}
