import java.util.List;

// GEGEBEN - nicht aendern.
// Eine Auswahlstrategie entscheidet, welche Drohne einen Auftrag bekommt.
public abstract class Auswahlstrategie {

    /** Gibt die Drohne zurueck, die den Auftrag uebernehmen soll, oder null, wenn keine ihn schafft. */
    public abstract Drohne waehle(List<Drohne> flotte, Auftrag auftrag, Wetter wetter);
}
