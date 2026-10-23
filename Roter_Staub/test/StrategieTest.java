import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StrategieTest {

    private Drohne rex;
    private Drohne atlas;
    private Drohne luna;
    private List<Drohne> flotte;

    @BeforeEach
    void setUp() {
        rex = new Schuerfdrohne("S-1", "Rex");
        atlas = new Transportdrohne("S-2", "Atlas");
        luna = new Aufklaerungsdrohne("S-3", "Luna");
        flotte = new ArrayList<>(List.of(rex, atlas, luna));
    }

    @Test
    @DisplayName("Auswahlstrategie ist abstrakt, beide Strategien erben von ihr")
    void auswahlstrategie_istAbstrakt() {
        assertTrue(Modifier.isAbstract(Auswahlstrategie.class.getModifiers()));
        assertTrue(Auswahlstrategie.class.isAssignableFrom(NaechsteDrohne.class));
        assertTrue(Auswahlstrategie.class.isAssignableFrom(SparsamsteDrohne.class));
    }

    @Test
    @DisplayName("NaechsteDrohne: waehlt die Drohne mit dem kuerzesten Weg zum Ziel")
    void naechste_waehltKuerzestenWeg() {
        // Rex steht jetzt bei 8/8, die anderen an der Basis
        rex.fuehreAus(new Auftrag("Vorab", Ressource.EIS, 1, new Koordinate(8, 8)), Wetter.KLAR);
        // Ziel 7/8: Rex ist 1 Feld weg, Atlas 15, Luna 15
        Auftrag auftrag = new Auftrag("Nahe Rex", Ressource.EIS, 1, new Koordinate(7, 8));

        assertSame(rex, new NaechsteDrohne().waehle(flotte, auftrag, Wetter.KLAR));
    }

    @Test
    @DisplayName("NaechsteDrohne: ueberspringt eine nahe Drohne, die zu wenig Energie hat")
    void naechste_ueberspringtSchwacheDrohnen() {
        // Rex fuhr weit raus (Bedarf 91) und hat nur noch 9 Energie, steht bei 45/0
        rex.fuehreAus(new Auftrag("Vorab", Ressource.EIS, 1, new Koordinate(45, 0)), Wetter.KLAR);
        // Ziel 50/5: Rex ist am naechsten (10 Felder), braucht aber 21 Energie.
        // Atlas braucht 165 (hat 150), Luna braucht 55 (hat 60).
        Auftrag auftrag = new Auftrag("Weit draussen", Ressource.EIS, 1, new Koordinate(50, 5));

        assertSame(luna, new NaechsteDrohne().waehle(flotte, auftrag, Wetter.KLAR));
    }

    @Test
    @DisplayName("NaechsteDrohne: keine Drohne schafft den Auftrag ergibt null")
    void naechste_niemandSchaffts_null() {
        Auftrag zuWeit = new Auftrag("Zu weit", Ressource.EIS, 1, new Koordinate(70, 0));

        assertNull(new NaechsteDrohne().waehle(flotte, zuWeit, Wetter.KLAR));
    }

    @Test
    @DisplayName("NaechsteDrohne: bei Gleichstand gewinnt die erste in der Liste")
    void naechste_gleichstand_ersteGewinnt() {
        Auftrag auftrag = new Auftrag("Alle gleich weit", Ressource.EIS, 1, new Koordinate(2, 2));

        assertSame(rex, new NaechsteDrohne().waehle(flotte, auftrag, Wetter.KLAR));
    }

    @Test
    @DisplayName("SparsamsteDrohne: waehlt den kleinsten Energiebedarf (Luna, 1 pro Feld)")
    void sparsamste_waehltKleinstenBedarf() {
        Auftrag auftrag = new Auftrag("Test", Ressource.ERZ, 5, new Koordinate(3, 4));

        assertSame(luna, new SparsamsteDrohne().waehle(flotte, auftrag, Wetter.KLAR));
    }

    @Test
    @DisplayName("SparsamsteDrohne: ueberspringt die sparsamste Drohne, wenn sie zu wenig Energie hat")
    void sparsamste_ueberspringtSchwacheDrohnen() {
        // Luna fuhr weit raus (Bedarf 58) und hat nur noch 2 Energie
        luna.fuehreAus(new Auftrag("Vorab", Ressource.EIS, 1, new Koordinate(58, 0)), Wetter.KLAR);
        // Ziel 3/4: Rex braucht 19, Atlas 21, Luna waere 59 Felder weit weg
        Auftrag auftrag = new Auftrag("Nahes Erz", Ressource.ERZ, 5, new Koordinate(3, 4));

        assertSame(rex, new SparsamsteDrohne().waehle(flotte, auftrag, Wetter.KLAR));
    }

    @Test
    @DisplayName("SparsamsteDrohne: keine Drohne schafft den Auftrag ergibt null")
    void sparsamste_niemandSchaffts_null() {
        Auftrag zuWeit = new Auftrag("Zu weit", Ressource.EIS, 1, new Koordinate(70, 0));

        assertNull(new SparsamsteDrohne().waehle(flotte, zuWeit, Wetter.KLAR));
    }

    @Test
    @DisplayName("Strategien: leere Flotte ergibt null")
    void leereFlotte_ergibtNull() {
        Auftrag auftrag = new Auftrag("Egal", Ressource.EIS, 1, new Koordinate(1, 1));

        assertNull(new NaechsteDrohne().waehle(new ArrayList<>(), auftrag, Wetter.KLAR));
        assertNull(new SparsamsteDrohne().waehle(new ArrayList<>(), auftrag, Wetter.KLAR));
    }
}
