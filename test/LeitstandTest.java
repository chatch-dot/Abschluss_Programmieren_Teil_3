import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LeitstandTest {

    private Leitstand leitstand;
    private Drohne rex;
    private Drohne atlas;
    private Drohne luna;

    @BeforeEach
    void setUp() {
        Missionslog.getInstance().leeren();
        leitstand = new Leitstand();
        rex = new Schuerfdrohne("L-1", "Rex");
        atlas = new Transportdrohne("L-2", "Atlas");
        luna = new Aufklaerungsdrohne("L-3", "Luna");
        leitstand.aufnehmen(rex);
        leitstand.aufnehmen(atlas);
        leitstand.aufnehmen(luna);
    }

    @Test
    @DisplayName("aufnehmen: neue Drohnen kommen in die Flotte")
    void aufnehmen_neueDrohne() {
        assertEquals(3, leitstand.getFlotte().size());
        assertTrue(leitstand.aufnehmen(new Schuerfdrohne("L-4", "Neu")));
        assertEquals(4, leitstand.getFlotte().size());
    }

    @Test
    @DisplayName("aufnehmen: eine Drohne mit gleicher Seriennummer wird abgelehnt (equals)")
    void aufnehmen_doppelteDrohne_abgelehnt() {
        boolean aufgenommen = leitstand.aufnehmen(new Schuerfdrohne("L-1", "Rex Zwilling"));

        assertFalse(aufgenommen);
        assertEquals(3, leitstand.getFlotte().size());
    }

    @Test
    @DisplayName("getFlotte gibt eine Kopie heraus")
    void getFlotte_istKopie() {
        leitstand.getFlotte().clear();

        assertEquals(3, leitstand.getFlotte().size());
    }

    @Test
    @DisplayName("Standard: Wetter KLAR und Strategie NaechsteDrohne")
    void standardWerte() {
        assertEquals(Wetter.KLAR, leitstand.getWetter());

        // Rex steht bei 8/8. Ein Auftrag bei 8/9 geht dann an Rex (naechste Drohne).
        rex.fuehreAus(new Auftrag("Vorab", Ressource.EIS, 1, new Koordinate(8, 8)), Wetter.KLAR);
        leitstand.vergebe(new Auftrag("Nebenan", Ressource.EIS, 1, new Koordinate(8, 9)));

        assertEquals(new Koordinate(8, 9), rex.getStandort());
    }

    @Test
    @DisplayName("vergebe: die Strategie entscheidet, wer fliegt (Sparsamste: Luna)")
    void vergebe_nutztDieStrategie() {
        leitstand.setStrategie(new SparsamsteDrohne());
        Auftrag auftrag = new Auftrag("Test", Ressource.ERZ, 5, new Koordinate(3, 4));

        Einsatzergebnis ergebnis = leitstand.vergebe(auftrag);

        assertTrue(ergebnis.erfolgreich());
        assertEquals(new Koordinate(3, 4), luna.getStandort());
        assertEquals(rex.getStandort(), Koordinate.BASIS);
    }

    @Test
    @DisplayName("vergebe: die Strategie laesst sich zur Laufzeit tauschen")
    void vergebe_strategieTauschen() {
        Auftrag auftrag = new Auftrag("Test", Ressource.ERZ, 5, new Koordinate(3, 4));

        leitstand.setStrategie(new SparsamsteDrohne());
        leitstand.vergebe(auftrag);
        assertEquals(Koordinate.BASIS, atlas.getStandort());

        leitstand.setStrategie(new NaechsteDrohne());
        leitstand.vergebe(new Auftrag("Direkt daneben", Ressource.EIS, 1, new Koordinate(3, 5)));
        // Luna steht schon bei 3/4 und ist am naechsten
        assertEquals(new Koordinate(3, 5), luna.getStandort());
    }

    @Test
    @DisplayName("vergebe: Das Wetter des Leitstands zaehlt (Staub macht den Einsatz teurer)")
    void vergebe_beruecksichtigtWetter() {
        leitstand.setWetter(Wetter.STAUB);
        leitstand.setStrategie(new SparsamsteDrohne());
        Auftrag auftrag = new Auftrag("Test", Ressource.ERZ, 5, new Koordinate(3, 4));

        Einsatzergebnis ergebnis = leitstand.vergebe(auftrag);

        // Luna: Basis 7, Zuschlag 25 Prozent = 1 (ganzzahlig), also 8
        assertEquals(8, ergebnis.energieVerbrauch());
    }

    @Test
    @DisplayName("vergebe: schafft keine Drohne den Auftrag, ist das Ergebnis nicht erfolgreich")
    void vergebe_niemandSchaffts() {
        Auftrag zuWeit = new Auftrag("Zu weit", Ressource.EIS, 1, new Koordinate(90, 90));

        Einsatzergebnis ergebnis = leitstand.vergebe(zuWeit);

        assertFalse(ergebnis.erfolgreich());
        assertEquals(0, ergebnis.ertrag());
        assertFalse(ergebnis.meldung() == null || ergebnis.meldung().isBlank());
    }

    @Test
    @DisplayName("Gesamtertrag: Summe aller erfolgreichen Auftraege (20 + 18 = 38)")
    void gesamtErtrag_summiert() {
        assertEquals(0, leitstand.getGesamtErtrag());

        // Alle stehen an der Basis, gleicher Abstand: Rex (erste Drohne) nimmt beide Auftraege
        leitstand.vergebe(new Auftrag("A", Ressource.ERZ, 4, new Koordinate(2, 3)));
        leitstand.vergebe(new Auftrag("B", Ressource.SILIZIUM, 2, new Koordinate(2, 4)));

        assertEquals(38, leitstand.getGesamtErtrag());
    }

    @Test
    @DisplayName("Gesamtertrag: fehlgeschlagene Auftraege zaehlen nicht")
    void gesamtErtrag_ignoriertFehlschlaege() {
        leitstand.vergebe(new Auftrag("Zu weit", Ressource.SILIZIUM, 9, new Koordinate(90, 90)));

        assertEquals(0, leitstand.getGesamtErtrag());
    }

    @Test
    @DisplayName("Missionslog: jeder Auftrag ergibt genau einen Eintrag, auch bei Fehlschlag")
    void vergebe_schreibtInsMissionslog() {
        leitstand.vergebe(new Auftrag("Erfolg", Ressource.EIS, 1, new Koordinate(1, 1)));
        leitstand.vergebe(new Auftrag("Fehlschlag", Ressource.EIS, 1, new Koordinate(90, 90)));

        List<String> eintraege = Missionslog.getInstance().getEintraege();
        assertEquals(2, eintraege.size());
        assertTrue(eintraege.get(0).contains("Erfolg"));
        assertTrue(eintraege.get(1).contains("Fehlschlag"));
    }

    @Test
    @DisplayName("alleLaden: danach hat jede Drohne wieder volle Energie")
    void alleLaden_fuelltAlleAuf() {
        leitstand.vergebe(new Auftrag("A", Ressource.ERZ, 4, new Koordinate(2, 3)));
        assertTrue(rex.getEnergie() < 100);

        leitstand.alleLaden();

        assertEquals(100, rex.getEnergie());
        assertEquals(150, atlas.getEnergie());
        assertEquals(60, luna.getEnergie());
    }
}
