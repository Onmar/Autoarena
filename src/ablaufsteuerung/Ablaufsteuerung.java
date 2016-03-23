package ablaufsteuerung;

import java.util.concurrent.TimeUnit;

import io.IOHandler;
import master.Globals;

public class Ablaufsteuerung {

	private static ZustaendeSpiel zustandSpiel = ZustaendeSpiel.SPIEL_AUS;
	private static ZustaendeBall zustandBall = ZustaendeBall.KEIN_BALL;
	private static long lastMilis;

	public static ZustaendeSpiel getGameState() {
		return zustandSpiel;
	}

	private static void laufendesSpiel() {
		switch (zustandBall) {
		case KEIN_BALL:
			if (IOHandler.spieler1_NeuerBall && IOHandler.spieler2_NeuerBall) {
				zustandBall = ZustaendeBall.BALL_EINWURF;
			}
			break;
		case BALL_EINWURF:
			io.IOHandler.ball_Motor = true;
			if (io.IOHandler.ball_BallEingeworfen) {
				io.IOHandler.ball_Motor = false;
				zustandBall = ZustaendeBall.BALL_VORHANDEN;
				lastMilis = System.currentTimeMillis();
			}
			break;
		case BALL_VORHANDEN:
			long currentMilis = System.currentTimeMillis();
			Globals.spielzeit += (currentMilis - lastMilis);
			lastMilis = currentMilis;
			if (io.IOHandler.spieler1_Tor) {
				Globals.toreSpieler2++;
				zustandBall = ZustaendeBall.KEIN_BALL;
			} else {
				if (io.IOHandler.spieler2_Tor) {
					Globals.toreSpieler1++;
					zustandBall = ZustaendeBall.KEIN_BALL;
				}
			}
			break;
		}
	}

	// Erstellen aller Verbindungen
	public static void init() {
		IOHandler.init(true);
	}

	// Statusmaschine
	public static void run() {
		switch (zustandSpiel) {
		case SPIEL_AUS:
			if (IOHandler.spieler1_NeuerBall && IOHandler.spieler2_NeuerBall) {
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
			if ((Globals.spielzeit > TimeUnit.MINUTES.toMillis(5) || Globals.toreSpieler1 + Globals.toreSpieler2 >= 11)
					&& (zustandBall == ZustaendeBall.KEIN_BALL || zustandBall == ZustaendeBall.KEIN_BALL)) {
				
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
				}
				zustandSpiel = ZustaendeSpiel.SPIEL_FERTIG;
			}
			break;
		case SPIEL_FERTIG:
			Globals.einparken = true;
			if (Globals.mBotsGeparkt) {
				Globals.einparken = false;
				Globals.mBotsGeparkt = false;
				Globals.toreSpieler1 = 0;
				Globals.toreSpieler2 = 0;
				Globals.spielzeit = 0;
				Globals.round++;
				zustandSpiel = ZustaendeSpiel.SPIEL_AUS;
			}
			break;
		}
	}

	public static void close() {
		IOHandler.closeAll();
	}

}
