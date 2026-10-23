import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimulationTest {

    private Leitstand baueLeitstand() {
        Leitstand leitstand = new Leitstand();
        leitstand.aufnehmen(new Schuerfdrohne("SIM-1", "Rex"));
        leitstand.aufnehmen(new Transportdrohne("SIM-2", "Atlas"));
        leitstand.aufnehmen(new Aufklaerungsdrohne("SIM-3", "Luna"));
        return leitstand;
    }

    @BeforeEach
    void setUp() {
        Missionslog.getInstance().leeren();
    }

    @Test
    @DisplayName("Seed 42, 10 Auftraege, Strategie NaechsteDrohne: Gesamtertrag 147")
    void seed42_naechsteDrohne() {
        int ertrag = Simulation.simuliere(baueLeitstand(), 10, new Random(42));

        assertEquals(147, ertrag);
    }

    @Test
    @DisplayName("Seed 42, 10 Auftraege, Strategie SparsamsteDrohne: Gesamtertrag 36")
    void seed42_sparsamsteDrohne() {
        Leitstand leitstand = baueLeitstand();
        leitstand.setStrategie(new SparsamsteDrohne());

        int ertrag = Simulation.simuliere(leitstand, 10, new Random(42));

        assertEquals(36, ertrag);
    }

    @Test
    @DisplayName("Gleicher Seed ergibt gleiches Ergebnis")
    void gleicherSeed_gleichesErgebnis() {
        int erster = Simulation.simuliere(baueLeitstand(), 10, new Random(7));
        int zweiter = Simulation.simuliere(baueLeitstand(), 10, new Random(7));

        assertEquals(erster, zweiter);
    }

    @Test
    @DisplayName("Jeder Auftrag steht im Missionslog (10 Auftraege, 10 Eintraege)")
    void jederAuftragImLog() {
        Simulation.simuliere(baueLeitstand(), 10, new Random(42));

        assertEquals(10, Missionslog.getInstance().anzahl());
    }

    @Test
    @DisplayName("Keine Drohne hat am Ende negative Energie, der Ertrag ist nie negativ")
    void energieNieNegativ() {
        Leitstand leitstand = baueLeitstand();

        int ertrag = Simulation.simuliere(leitstand, 50, new Random(1));

        assertTrue(ertrag >= 0);
        for (Drohne drohne : leitstand.getFlotte()) {
            assertTrue(drohne.getEnergie() >= 0);
        }
    }

    @Test
    @DisplayName("Ohne Auftraege passiert nichts")
    void nullAuftraege() {
        assertEquals(0, Simulation.simuliere(baueLeitstand(), 0, new Random(42)));
    }
}
