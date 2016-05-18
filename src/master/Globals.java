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
    public static boolean spielLaeuft = false;
    
    // momentane spielzeit in ms
    public static long spielzeit = 0;
    public static long ausparkzeit = 0;
    // maximale Spielzeit in ms
    public static final long maxSpielzeit = TimeUnit.MINUTES.toMillis(3);
    public static final long maxAusparkzeit = TimeUnit.MINUTES.toMillis(15);
    
    // Scoreboard Variablen
    public static int toreSpieler1 = 0;
    public static int toreSpieler2 = 0;
    public static Colors mBotSpieler1 = Colors.WHITE;
    public static Colors mBotSpieler2 = Colors.WHITE;
    
    public static int round = 0;
    
    public static boolean stop = false;
    
    // mBot MAC-Adressen
    public static final String[] mBotAddresses = new String[] { "000502031DD3", "000D190306E7", "000D19000B08" };

    
    
    public static ZustaendeSpiel debug_spielZustand = ZustaendeSpiel.SPIEL_AUS;
    public static ZustaendeBall debug_ballZustand = ZustaendeBall.KEIN_BALL;
}
