import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;
import static org.junit.platform.launcher.EngineFilter.includeEngines;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

import java.nio.file.Path;
import java.util.Set;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherFactory;

/**
 * Die Test-Runtime: Das ist die "Main" fuer alle Tests.
 * Gruener Pfeil neben "main" klicken, fertig.
 *
 * Ohne Eingabe laufen alle Testklassen in diesem Ordner.
 * Nur eine Klasse? Den Namen als Programm-Argument uebergeben, zum Beispiel: HeldTest
 *
 * Wie das innen funktioniert, muss man nicht verstehen. Wichtig ist nur die Ausgabe.
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
        LauncherDiscoveryRequest anfrage = baueAnfrage(args);
        Bericht bericht = new Bericht();

        LauncherFactory.create().execute(anfrage, bericht);

        bericht.zusammenfassung();
        if (bericht.rot > 0 || bericht.gruen == 0) {
            System.exit(1);
        }
    }

    private static LauncherDiscoveryRequest baueAnfrage(String[] args) throws Exception {
        if (args.length > 0) {
            var builder = request();
            for (String klassenName : args) {
                builder.selectors(selectClass(Class.forName(klassenName)));
            }
            return builder.build();
        }
        // Alle Tests, die neben dieser Datei uebersetzt wurden
        Path testOrdner = Path.of(TestMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        return request()
                .selectors(selectClasspathRoots(Set.of(testOrdner)))
                .filters(includeEngines("junit-jupiter"))
                .build();
    }

    private static class Bericht implements TestExecutionListener {

        int gruen = 0;
        int rot = 0;

        @Override
        public void executionFinished(TestIdentifier test, TestExecutionResult ergebnis) {
            boolean istTest = test.isTest();
            boolean klasseKaputt = test.isContainer()
                    && ergebnis.getStatus() == TestExecutionResult.Status.FAILED;
            if (!istTest && !klasseKaputt) {
                return;
            }

            String klasse = test.getSource()
                    .filter(quelle -> quelle instanceof MethodSource)
                    .map(quelle -> ((MethodSource) quelle).getClassName())
                    .orElse(test.getParentId().orElse(""));

            if (ergebnis.getStatus() == TestExecutionResult.Status.SUCCESSFUL) {
                gruen++;
                System.out.println("  OK      " + klasse + ": " + test.getDisplayName());
                return;
            }

            rot++;
            System.out.println("  FEHLER  " + klasse + ": " + test.getDisplayName());
            ergebnis.getThrowable().ifPresent(fehler -> {
                System.out.println("          " + fehler.getMessage());
                for (StackTraceElement zeile : fehler.getStackTrace()) {
                    String name = zeile.getClassName();
                    if (!name.startsWith("org.junit") && !name.startsWith("java.") && !name.startsWith("jdk.")) {
                        System.out.println("          bei " + zeile.getFileName() + ":" + zeile.getLineNumber());
                        break;
                    }
                }
            });
        }

        void zusammenfassung() {
            System.out.println();
            if (gruen + rot == 0) {
                System.out.println("KEINE TESTS GEFUNDEN. Pruefen: @Test ueber der Methode? Import org.junit.jupiter.api.Test?");
                System.out.println("Liegt die Testklasse im Ordner test und wurde sie uebersetzt?");
            } else if (rot == 0) {
                System.out.println("ALLES GRUEN: " + gruen + " Tests erfolgreich.");
            } else {
                System.out.println("ROT: " + rot + " Tests fehlgeschlagen, " + gruen + " erfolgreich.");
            }
        }
    }
}
