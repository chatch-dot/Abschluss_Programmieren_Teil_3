import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FabrikTest {

    @Test
    @DisplayName("DrohnenTyp ist ein enum mit SCHUERF, TRANSPORT, AUFKLAERUNG")
    void drohnenTyp_istEnum() {
        assertTrue(DrohnenTyp.class.isEnum());
        assertEquals(3, DrohnenTyp.values().length);
    }

    @Test
    @DisplayName("Fabrik baut je nach Typ die richtige Klasse")
    void baue_liefertRichtigeKlasse() {
        assertTrue(DrohnenFabrik.baue(DrohnenTyp.SCHUERF, "A") instanceof Schuerfdrohne);
        assertTrue(DrohnenFabrik.baue(DrohnenTyp.TRANSPORT, "B") instanceof Transportdrohne);
        assertTrue(DrohnenFabrik.baue(DrohnenTyp.AUFKLAERUNG, "C") instanceof Aufklaerungsdrohne);
    }

    @Test
    @DisplayName("Fabrik uebernimmt den Namen")
    void baue_uebernimmtName() {
        Drohne drohne = DrohnenFabrik.baue(DrohnenTyp.SCHUERF, "Rex");

        assertEquals("Rex", drohne.getName());
    }

    @Test
    @DisplayName("Jede gebaute Drohne bekommt eine eigene Seriennummer")
    void baue_vergibtEindeutigeSeriennummern() {
        Drohne a = DrohnenFabrik.baue(DrohnenTyp.SCHUERF, "Rex");
        Drohne b = DrohnenFabrik.baue(DrohnenTyp.SCHUERF, "Rex");

        assertNotEquals(a.getSeriennummer(), b.getSeriennummer());
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("Seriennummern beginnen mit ARES-")
    void baue_seriennummerHatPraefix() {
        Drohne drohne = DrohnenFabrik.baue(DrohnenTyp.TRANSPORT, "Atlas");

        assertTrue(drohne.getSeriennummer().startsWith("ARES-"));
    }

    @Test
    @DisplayName("Die Fabrik zaehlt mit: der static Zaehler steigt pro Drohne um 1")
    void baue_erhoehtZaehler() {
        int vorher = Drohne.getAnzahlGebaut();

        DrohnenFabrik.baue(DrohnenTyp.AUFKLAERUNG, "Luna");

        assertEquals(vorher + 1, Drohne.getAnzahlGebaut());
    }

    @Test
    @DisplayName("Ohne Typ (null) gibt es eine IllegalArgumentException")
    void baue_ohneTyp_wirftFehler() {
        assertThrows(IllegalArgumentException.class, () -> DrohnenFabrik.baue(null, "Rex"));
    }
}
