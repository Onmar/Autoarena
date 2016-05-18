package master;

import java.util.concurrent.TimeUnit;

import ablaufsteuerung.ZustaendeBall;
import ablaufsteuerung.ZustaendeSpiel;
import display.Colors;

public class Globals {
	
	// Kommandos
    public static boolean ausparken = false;
    public static boolean einparken = false;
    public static boolean mBotsBereit = false;
    public static boolean mBotsGeparkt = false;
    
    // Momentane spielzeit in ms
    public static long spielzeit = 0;
    // Zeit, bei der die Roboter ausgeparkt wurden in ms 
    public static long ausparkzeit = 0;
    // maximale Spielzeit in ms
    public static final long maxSpielzeit = TimeUnit.MINUTES.toMillis(3);
    // Maximale Zeit die die Roboter Ausparkiert sind in ms
    public static final long maxAusparkzeit = TimeUnit.MINUTES.toMillis(15);
    // Maximale Zeit in der nichts passiert, bis Spiel zurückgesetzt wird in ms
    public static final long maxWartezeit = TimeUnit.MINUTES.toMillis(1);
    
    // Scoreboard Variablen
    public static int toreSpieler1 = 0;
    public static int toreSpieler2 = 0;
    public static Colors mBotSpieler1 = Colors.WHITE;
    public static Colors mBotSpieler2 = Colors.WHITE;
    
    // Momentane Runde
    public static int round = 0;
    
    // Stoppt die Programmausführung
    public static boolean stop = false;
    
    // mBot MAC-Adressen
    public static final String[] mBotAddresses = new String[] { "000502031DD3", "000D190306E7", "000D19000B08" };

    
    // Debug-Variablen zum Testen der Anzeige
    public static ZustaendeSpiel debug_spielZustand = ZustaendeSpiel.SPIEL_AUS;
    public static ZustaendeBall debug_ballZustand = ZustaendeBall.KEIN_BALL;
}
