package ablaufsteuerung;

import java.util.concurrent.TimeUnit;

import io.IOHandler;
import master.Globals;

public class Ablaufsteuerung {

    private static boolean endProgram = false;
    private static ZustaendeSpiel zustandSpiel = ZustaendeSpiel.SPIEL_AUS;
    private static ZustaendeBall zustandBall = ZustaendeBall.KEIN_BALL;
    private static long lastMilis;
    
    private static void laufendesSpiel() {
        switch (zustandBall) {
        case KEIN_BALL:
            if(IOHandler.spieler1_NeuerBall && IOHandler.spieler2_NeuerBall) {
                zustandBall = ZustaendeBall.BALL_EINWURF;
            }
            break;
        case BALL_EINWURF:
            io.IOHandler.ball_Motor = true;
            if(io.IOHandler.ball_BallEingeworfen) {
                io.IOHandler.ball_Motor = false;
                zustandBall = ZustaendeBall.BALL_VORHANDEN;
                lastMilis = System.currentTimeMillis();
            }
            break;
        case BALL_VORHANDEN:
            long currentMilis = System.currentTimeMillis();
            Globals.spielzeit += (currentMilis - lastMilis);
            lastMilis = currentMilis;
            if(io.IOHandler.spieler1_Tor) {
                zustandBall = ZustaendeBall.TOR_SPIELER2;
            } else {
                if(io.IOHandler.spieler2_Tor) {
                    zustandBall = ZustaendeBall.TOR_SPIELER2;
                }
            }
            break;
        case TOR_SPIELER1:
            Globals.toreSpieler1++;
            zustandBall = ZustaendeBall.KEIN_BALL;
            break;
        case TOR_SPIELER2:
            Globals.toreSpieler2++;
            zustandBall = ZustaendeBall.KEIN_BALL;
            break;
        }
    }
    
    public static void stop() {
        endProgram = true;
    }

    // Main Program
    public static void run() {
        while (!endProgram) {
            switch (zustandSpiel) {
            case SPIEL_AUS:
                if (IOHandler.general_SpielStarten) {
                    zustandSpiel = ZustaendeSpiel.SPIEL_INIT;
                }
                break;
            case SPIEL_INIT:
                Globals.ausparken = true;
                if (Globals.mBotsBereit) {
                    Globals.ausparken = false;
                    Globals.mBotsBereit = false;
                    zustandSpiel = ZustaendeSpiel.SPIEL_LAEUFT;
                }
                break;
            case SPIEL_LAEUFT:
                laufendesSpiel();
                if ((Globals.spielzeit > (5 * 60 * 1000) || Globals.toreSpieler1 + Globals.toreSpieler2 >= 11) && (zustandBall == ZustaendeBall.TOR_SPIELER1 || zustandBall == ZustaendeBall.TOR_SPIELER2)) { // 5 Minutes in ms
                    Globals.toreSpieler1 = 0;
                    Globals.toreSpieler2 = 0;
                    Globals.spielzeit = 0;
                    zustandSpiel = ZustaendeSpiel.SPIEL_FERTIG;
                }
                break;
            case SPIEL_FERTIG:
                Globals.einparken = true;
                if (Globals.mBotsGeparkt) {
                    Globals.einparken = false;
                    Globals.mBotsGeparkt = false;
                    zustandSpiel = ZustaendeSpiel.SPIEL_AUS;
                }
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                //Handle exception
            }
        }
        endProgram = false;
    }

}
