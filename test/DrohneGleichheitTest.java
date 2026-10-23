import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DrohneGleichheitTest {

    @Test
    @DisplayName("equals: gleiche Seriennummer und gleiche Art bedeutet gleich, auch bei anderem Namen")
    void equals_gleicheSeriennummer() {
        Drohne a = new Schuerfdrohne("G-1", "Rex");
        Drohne b = new Schuerfdrohne("G-1", "Anderer Name");

        assertEquals(a, b);
    }

    @Test
    @DisplayName("equals: andere Seriennummer bedeutet ungleich")
    void equals_andereSeriennummer() {
        Drohne a = new Schuerfdrohne("G-1", "Rex");
        Drohne b = new Schuerfdrohne("G-2", "Rex");

        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("equals: gleiche Seriennummer, aber andere Art, ist ungleich")
    void equals_andereArt() {
        Drohne a = new Schuerfdrohne("G-1", "Rex");
        Drohne b = new Transportdrohne("G-1", "Rex");

        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("equals: null und fremde Typen sind ungleich, ohne Absturz")
    void equals_nullUndFremderTyp() {
        Drohne a = new Schuerfdrohne("G-1", "Rex");

        assertFalse(a.equals(null));
        assertFalse(a.equals("G-1"));
    }

    @Test
    @DisplayName("equals: eine Drohne ist gleich zu sich selbst")
    void equals_reflexiv() {
        Drohne a = new Aufklaerungsdrohne("G-1", "Luna");

        assertTrue(a.equals(a));
    }

    @Test
    @DisplayName("hashCode: gleiche Drohnen haben denselben hashCode")
    void hashCode_zuEqualsPassend() {
        Drohne a = new Schuerfdrohne("G-1", "Rex");
        Drohne b = new Schuerfdrohne("G-1", "Rex");

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("HashSet: dieselbe Drohne zweimal ergibt nur einen Eintrag")
    void hashSet_erkenntDuplikate() {
        Set<Drohne> menge = new HashSet<>();

        menge.add(new Schuerfdrohne("G-1", "Rex"));
        menge.add(new Schuerfdrohne("G-1", "Rex"));
        menge.add(new Schuerfdrohne("G-2", "Rex"));

        assertEquals(2, menge.size());
    }

    @Test
    @DisplayName("toString enthaelt Name und Seriennummer")
    void toString_enthaeltNameUndSeriennummer() {
        Drohne a = new Transportdrohne("G-77", "Atlas");

        assertTrue(a.toString().contains("Atlas"));
        assertTrue(a.toString().contains("G-77"));
    }
}
