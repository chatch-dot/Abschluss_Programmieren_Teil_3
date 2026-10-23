import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Getestet wird die Basisklasse ueber eine Schuerfdrohne (100 Energie, 2 pro Feld + Menge). */
class DrohneTest {

    private Drohne rex;
    private Auftrag nahesErz;

    @BeforeEach
    void setUp() {
        rex = new Schuerfdrohne("T-1", "Rex");
        nahesErz = new Auftrag("Nahes Erz", Ressource.ERZ, 4, new Koordinate(2, 3));
    }

    @Test
    @DisplayName("Drohne ist abstrakt, die drei Arten erben von ihr")
    void drohne_istAbstrakt() {
        assertTrue(Modifier.isAbstract(Drohne.class.getModifiers()));
        assertTrue(Drohne.class.isAssignableFrom(Schuerfdrohne.class));
        assertTrue(Drohne.class.isAssignableFrom(Transportdrohne.class));
        assertTrue(Drohne.class.isAssignableFrom(Aufklaerungsdrohne.class));
    }

    @Test
    @DisplayName("Neue Drohne: Seriennummer, Name, volle Energie, Standort Basis")
    void neueDrohne_hatStartwerte() {
        assertEquals("T-1", rex.getSeriennummer());
        assertEquals("Rex", rex.getName());
        assertEquals(100, rex.getEnergie());
        assertEquals(Koordinate.BASIS, rex.getStandort());
    }

    @Test
    @DisplayName("static: jede gebaute Drohne zaehlt den Zaehler um 1 hoch")
    void anzahlGebaut_zaehltHoch() {
        int vorher = Drohne.getAnzahlGebaut();

        new Transportdrohne("T-2", "Atlas");
        new Aufklaerungsdrohne("T-3", "Luna");

        assertEquals(vorher + 2, Drohne.getAnzahlGebaut());
    }

    @Test
    @DisplayName("Energiebedarf bei klarem Wetter ist der Basisbedarf (2*5 + 4 = 14)")
    void energieBedarf_klar() {
        assertEquals(14, rex.energieBedarf(nahesErz, Wetter.KLAR));
    }

    @Test
    @DisplayName("Energiebedarf bei Staub: 25 Prozent Zuschlag, ganzzahlig (14 + 3 = 17)")
    void energieBedarf_staub() {
        assertEquals(17, rex.energieBedarf(nahesErz, Wetter.STAUB));
    }

    @Test
    @DisplayName("Energiebedarf bei Sturm: doppelter Bedarf (28)")
    void energieBedarf_sturm() {
        assertEquals(28, rex.energieBedarf(nahesErz, Wetter.STURM));
    }

    @Test
    @DisplayName("fuehreAus: Erfolg, Energie sinkt, Standort ist das Ziel, Ertrag stimmt")
    void fuehreAus_erfolg() {
        Einsatzergebnis ergebnis = rex.fuehreAus(nahesErz, Wetter.KLAR);

        assertTrue(ergebnis.erfolgreich());
        assertEquals(14, ergebnis.energieVerbrauch());
        assertEquals(20, ergebnis.ertrag());
        assertEquals(86, rex.getEnergie());
        assertEquals(new Koordinate(2, 3), rex.getStandort());
    }

    @Test
    @DisplayName("fuehreAus: Die Meldung ist nicht leer")
    void fuehreAus_meldungVorhanden() {
        Einsatzergebnis ergebnis = rex.fuehreAus(nahesErz, Wetter.KLAR);

        assertFalse(ergebnis.meldung() == null || ergebnis.meldung().isBlank());
    }

    @Test
    @DisplayName("fuehreAus mit zu wenig Energie: kein Erfolg, nichts veraendert")
    void fuehreAus_zuWenigEnergie_aendertNichts() {
        Auftrag weit = new Auftrag("Weit weg", Ressource.EIS, 10, new Koordinate(30, 30));

        Einsatzergebnis ergebnis = rex.fuehreAus(weit, Wetter.KLAR);

        assertFalse(ergebnis.erfolgreich());
        assertEquals(0, ergebnis.energieVerbrauch());
        assertEquals(0, ergebnis.ertrag());
        assertEquals(100, rex.getEnergie());
        assertEquals(Koordinate.BASIS, rex.getStandort());
    }

    @Test
    @DisplayName("Genau gleich viel Energie wie Bedarf reicht noch")
    void fuehreAus_genauGenugEnergie() {
        Auftrag knapp = new Auftrag("Knapp", Ressource.EIS, 10, new Koordinate(45, 0));
        // Bedarf: 2*45 + 10 = 100

        assertTrue(rex.kannDurchfuehren(knapp, Wetter.KLAR));
        assertTrue(rex.fuehreAus(knapp, Wetter.KLAR).erfolgreich());
        assertEquals(0, rex.getEnergie());
    }

    @Test
    @DisplayName("Wetter kann einen sonst moeglichen Auftrag verhindern")
    void kannDurchfuehren_sturmVerhindert() {
        Auftrag grenzfall = new Auftrag("Grenzfall", Ressource.EIS, 10, new Koordinate(25, 0));
        // Bedarf klar: 60, Sturm: 120

        assertTrue(rex.kannDurchfuehren(grenzfall, Wetter.KLAR));
        assertFalse(rex.kannDurchfuehren(grenzfall, Wetter.STURM));
    }

    @Test
    @DisplayName("Der Standort wandert: der naechste Weg startet am letzten Ziel")
    void fuehreAus_zweiterWegStartetAmLetztenZiel() {
        rex.fuehreAus(nahesErz, Wetter.KLAR);
        Auftrag nebenan = new Auftrag("Nebenan", Ressource.EIS, 1, new Koordinate(2, 4));

        assertEquals(3, rex.energieBedarf(nebenan, Wetter.KLAR));
    }

    @Test
    @DisplayName("laden fuellt die Energie wieder auf")
    void laden_fuelltAuf() {
        rex.fuehreAus(nahesErz, Wetter.KLAR);

        rex.laden();

        assertEquals(100, rex.getEnergie());
    }
}
