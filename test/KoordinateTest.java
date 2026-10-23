import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KoordinateTest {

    @Test
    @DisplayName("Koordinate ist ein record")
    void koordinate_istRecord() {
        assertTrue(Koordinate.class.isRecord());
    }

    @Test
    @DisplayName("BASIS ist die Koordinate 0/0")
    void basis_istNullNull() {
        assertEquals(0, Koordinate.BASIS.x());
        assertEquals(0, Koordinate.BASIS.y());
    }

    @Test
    @DisplayName("Abstand ist die Summe der Schritte in x und y (3 und 4 ergibt 7)")
    void abstandZu_summeDerSchritte() {
        Koordinate ziel = new Koordinate(3, 4);

        assertEquals(7, Koordinate.BASIS.abstandZu(ziel));
    }

    @Test
    @DisplayName("Abstand ist in beide Richtungen gleich")
    void abstandZu_istSymmetrisch() {
        Koordinate a = new Koordinate(2, 9);
        Koordinate b = new Koordinate(7, 1);

        assertEquals(13, a.abstandZu(b));
        assertEquals(13, b.abstandZu(a));
    }

    @Test
    @DisplayName("Abstand zu sich selbst ist 0")
    void abstandZu_sichSelbst_null() {
        Koordinate a = new Koordinate(5, 5);

        assertEquals(0, a.abstandZu(new Koordinate(5, 5)));
    }

    @Test
    @DisplayName("Negative Koordinaten: Abstand ist nie negativ")
    void abstandZu_negativeWerte() {
        Koordinate a = new Koordinate(-2, -3);
        Koordinate b = new Koordinate(1, 1);

        assertEquals(7, a.abstandZu(b));
    }

    @Test
    @DisplayName("Records vergleichen nach Inhalt")
    void equals_gleicheWerteSindGleich() {
        assertEquals(new Koordinate(1, 2), new Koordinate(1, 2));
        assertNotEquals(new Koordinate(1, 2), new Koordinate(2, 1));
    }
}
