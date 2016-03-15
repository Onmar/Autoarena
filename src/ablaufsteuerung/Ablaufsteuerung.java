package ablaufsteuerung;

import java.util.concurrent.TimeUnit;

import io.IOHandler;
import master.Globals;

public class Ablaufsteuerung {

    private static boolean endProgram = false;
    private static Zust�ndeSpiel zustandSpiel = Zust�ndeSpiel.SPIEL_AUS;
    private static Zust�ndeBall zustandBall = Zust�ndeBall.KEIN_BALL;
    private static long lastMilis;
    
    private static void laufendesSpiel() {
        switch (zustandBall) {
        case KEIN_BALL:
            if(IOHandler.spieler1_NeuerBall && IOHandler.spieler2_NeuerBall) {
                zustandBall = Zust�ndeBall.BALL_EINWURF;
            }
            break;
        case BALL_EINWURF:
            io.IOHandler.ball_Motor = true;
            if(io.IOHandler.ball_BallEingeworfen) {
                io.IOHandler.ball_Motor = false;
                zustandBall = Zust�ndeBall.BALL_VORHANDEN;
                lastMilis = System.currentTimeMillis();
            }
            break;
        case BALL_VORHANDEN:
            long currentMilis = System.currentTimeMillis();
            Globals.spielzeit += (currentMilis - lastMilis);
            lastMilis = currentMilis;
            if(io.IOHandler.spieler1_Tor) {
                zustandBall = Zust�ndeBall.TOR_SPIELER2;
            } else {
                if(io.IOHandler.spieler2_Tor) {
                    zustandBall = Zust�ndeBall.TOR_SPIELER2;
                }
            }
            break;
        case TOR_SPIELER1:
            Globals.toreSpieler1++;
            zustandBall = Zust�ndeBall.KEIN_BALL;
            break;
        case TOR_SPIELER2:
            Globals.toreSpieler2++;
            zustandBall = Zust�ndeBall.KEIN_BALL;
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
                    zustandSpiel = Zust�ndeSpiel.SPIEL_INIT;
                }
                break;
            case SPIEL_INIT:
                Globals.ausparken = true;
                if (Globals.mBotsBereit) {
                    Globals.ausparken = false;
                    Globals.mBotsBereit = false;
                    zustandSpiel = Zust�ndeSpiel.SPIEL_L�UFT;
                }
                break;
            case SPIEL_L�UFT:
                laufendesSpiel();
                if ((Globals.spielzeit > (5 * 60 * 1000) || Globals.toreSpieler1 + Globals.toreSpieler2 >= 11) && (zustandBall == Zust�ndeBall.TOR_SPIELER1 || zustandBall == Zust�ndeBall.TOR_SPIELER2)) { // 5 Minutes in ms
                    Globals.toreSpieler1 = 0;
                    Globals.toreSpieler2 = 0;
                    Globals.spielzeit = 0;
                    zustandSpiel = Zust�ndeSpiel.SPIEL_FERTIG;
                }
                break;
            case SPIEL_FERTIG:
                Globals.einparken = true;
                if (Globals.mBotsGeparkt) {
                    Globals.einparken = false;
                    Globals.mBotsGeparkt = false;
                    zustandSpiel = Zust�ndeSpiel.SPIEL_AUS;
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
