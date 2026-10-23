// Aufgabe 1.1 (2 Punkte)
// Jedes Wetter hat einen Energiezuschlag in Prozent: KLAR = 0, STAUB = 25, STURM = 100.
public enum Wetter {
    KLAR(0), STAUB(25), STURM(100);

    private final int zuschlagProzent;

    private Wetter(int zuschlagProzent)
    {
        this.zuschlagProzent = zuschlagProzent;
    }

    public int getZuschlagProzent()
    {
        return zuschlagProzent;
    }
}
