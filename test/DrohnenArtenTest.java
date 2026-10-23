import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DrohnenArtenTest {

    private Auftrag auftrag;

    @BeforeEach
    void setUp() {
        // 3 + 4 = 7 Felder von der Basis entfernt
        auftrag = new Auftrag("Testfeld", Ressource.ERZ, 5, new Koordinate(3, 4));
    }

    @Test
    @DisplayName("Schuerfdrohne: 100 Energie, Bedarf 2 pro Feld + Menge (19), Ertrag Menge * Wert (25)")
    void schuerfdrohne() {
        Drohne drohne = new Schuerfdrohne("A-1", "Rex");

        assertEquals(100, drohne.getEnergie());
        assertEquals(19, drohne.energieBedarf(auftrag, Wetter.KLAR));
        assertEquals(25, drohne.fuehreAus(auftrag, Wetter.KLAR).ertrag());
    }

    @Test
    @DisplayName("Transportdrohne: 150 Energie, Bedarf 3 pro Feld (21), Ertrag halber Wert, abgerundet (12)")
    void transportdrohne() {
        Drohne drohne = new Transportdrohne("A-2", "Atlas");

        assertEquals(150, drohne.getEnergie());
        assertEquals(21, drohne.energieBedarf(auftrag, Wetter.KLAR));
        assertEquals(12, drohne.fuehreAus(auftrag, Wetter.KLAR).ertrag());
    }

    @Test
    @DisplayName("Aufklaerungsdrohne: 60 Energie, Bedarf 1 pro Feld (7), kein Ertrag")
    void aufklaerungsdrohne() {
        Drohne drohne = new Aufklaerungsdrohne("A-3", "Luna");

        assertEquals(60, drohne.getEnergie());
        assertEquals(7, drohne.energieBedarf(auftrag, Wetter.KLAR));
        assertEquals(0, drohne.fuehreAus(auftrag, Wetter.KLAR).ertrag());
    }

    @Test
    @DisplayName("Schuerfdrohne: Silizium bringt mehr als Eis bei gleicher Menge")
    void schuerfdrohne_ertragHaengtVonRessourceAb() {
        Drohne drohne = new Schuerfdrohne("A-4", "Rex");
        Auftrag eis = new Auftrag("Eis", Ressource.EIS, 3, new Koordinate(1, 1));
        Auftrag silizium = new Auftrag("Silizium", Ressource.SILIZIUM, 3, new Koordinate(1, 1));

        assertEquals(6, drohne.fuehreAus(eis, Wetter.KLAR).ertrag());
        assertEquals(27, drohne.fuehreAus(silizium, Wetter.KLAR).ertrag());
    }

    @Test
    @DisplayName("Polymorphie: Eine Liste von Drohne, jede rechnet auf ihre Art (19 + 21 + 7 = 47)")
    void polymorphie_jedeDrohneRechnetAnders() {
        List<Drohne> flotte = new ArrayList<>();
        flotte.add(new Schuerfdrohne("A-5", "Rex"));
        flotte.add(new Transportdrohne("A-6", "Atlas"));
        flotte.add(new Aufklaerungsdrohne("A-7", "Luna"));

        int summe = 0;
        for (Drohne drohne : flotte) {
            summe += drohne.energieBedarf(auftrag, Wetter.KLAR);
        }

        assertEquals(47, summe);
    }

    @Test
    @DisplayName("Wetter wirkt bei jeder Art: Transportdrohne im Staub (21 + 5 = 26)")
    void wetterWirktBeiJederArt() {
        Drohne drohne = new Transportdrohne("A-8", "Atlas");

        assertEquals(26, drohne.energieBedarf(auftrag, Wetter.STAUB));
    }
}
