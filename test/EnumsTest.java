import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EnumsTest {

    @Test
    @DisplayName("Ressource ist ein enum mit den drei Werten EIS, ERZ, SILIZIUM")
    void ressource_istEnumMitDreiWerten() {
        assertTrue(Ressource.class.isEnum());
        assertEquals(3, Ressource.values().length);
        assertEquals(Ressource.EIS, Ressource.values()[0]);
        assertEquals(Ressource.ERZ, Ressource.values()[1]);
        assertEquals(Ressource.SILIZIUM, Ressource.values()[2]);
    }

    @Test
    @DisplayName("Ressource: Wert pro Einheit EIS 2, ERZ 5, SILIZIUM 9")
    void ressource_wertProEinheit() {
        assertEquals(2, Ressource.EIS.getWertProEinheit());
        assertEquals(5, Ressource.ERZ.getWertProEinheit());
        assertEquals(9, Ressource.SILIZIUM.getWertProEinheit());
    }

    @Test
    @DisplayName("Wetter ist ein enum mit KLAR, STAUB, STURM (in dieser Reihenfolge)")
    void wetter_istEnumMitDreiWerten() {
        assertTrue(Wetter.class.isEnum());
        assertEquals(3, Wetter.values().length);
        assertEquals(Wetter.KLAR, Wetter.values()[0]);
        assertEquals(Wetter.STAUB, Wetter.values()[1]);
        assertEquals(Wetter.STURM, Wetter.values()[2]);
    }

    @Test
    @DisplayName("Wetter: Zuschlag KLAR 0, STAUB 25, STURM 100 Prozent")
    void wetter_zuschlagProzent() {
        assertEquals(0, Wetter.KLAR.getZuschlagProzent());
        assertEquals(25, Wetter.STAUB.getZuschlagProzent());
        assertEquals(100, Wetter.STURM.getZuschlagProzent());
    }
}
