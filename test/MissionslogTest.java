import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MissionslogTest {

    @BeforeEach
    void setUp() {
        Missionslog.getInstance().leeren();
    }

    @Test
    @DisplayName("Singleton: getInstance liefert immer dasselbe Objekt")
    void getInstance_istImmerDasselbe() {
        assertSame(Missionslog.getInstance(), Missionslog.getInstance());
    }

    @Test
    @DisplayName("Singleton: der Konstruktor ist nicht von aussen erreichbar")
    void konstruktor_istPrivate() {
        var konstruktoren = Missionslog.class.getDeclaredConstructors();

        assertEquals(1, konstruktoren.length);
        assertEquals(true, Modifier.isPrivate(konstruktoren[0].getModifiers()));
    }

    @Test
    @DisplayName("Eintraege bleiben in der Reihenfolge erhalten")
    void eintragen_merktReihenfolge() {
        Missionslog.getInstance().eintragen("Start");
        Missionslog.getInstance().eintragen("Landung");

        assertEquals(List.of("Start", "Landung"), Missionslog.getInstance().getEintraege());
        assertEquals(2, Missionslog.getInstance().anzahl());
    }

    @Test
    @DisplayName("Zwei Zugriffe teilen sich dieselben Eintraege")
    void eintraege_sindGeteilt() {
        Missionslog.getInstance().eintragen("Eintrag A");

        assertEquals(1, Missionslog.getInstance().anzahl());
        assertEquals("Eintrag A", Missionslog.getInstance().getEintraege().get(0));
    }

    @Test
    @DisplayName("getEintraege gibt eine Kopie heraus: Aendern der Kopie aendert das Log nicht")
    void getEintraege_istKopie() {
        Missionslog.getInstance().eintragen("Original");

        Missionslog.getInstance().getEintraege().clear();

        assertEquals(1, Missionslog.getInstance().anzahl());
    }

    @Test
    @DisplayName("leeren entfernt alle Eintraege")
    void leeren_entferntAlles() {
        Missionslog.getInstance().eintragen("Etwas");

        Missionslog.getInstance().leeren();

        assertEquals(0, Missionslog.getInstance().anzahl());
    }
}
