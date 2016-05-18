package ablaufsteuerung;

import java.util.concurrent.TimeUnit;

import display.Colors;

import io.IOHandler;
import master.Globals;

public class Ablaufsteuerung {

	private static ZustaendeSpiel zustandSpiel = ZustaendeSpiel.SPIEL_AUS;
	private static ZustaendeBall zustandBall = ZustaendeBall.KEIN_BALL;
	private static long lastMilis;
	private static long gameStopTime;

	// IOHandler.ball_BallEingeworfen of last cycle
	private static boolean lastBallEingeworfen = true;

	public static ZustaendeSpiel getGameState() {
		return zustandSpiel;
	}

	public static ZustaendeBall getBallState() {
		return zustandBall;
	}

	private static void laufendesSpiel() {
		switch (zustandBall) {
		case KEIN_BALL:
			if (IOHandler.spieler1_NeuerBall && IOHandler.spieler2_NeuerBall) {
				zustandBall = ZustaendeBall.BALL_EINWURF;
			}
			break;
		case BALL_EINWURF:
			IOHandler.startBallMotor();
			if (IOHandler.ball_BallEingeworfen && !lastBallEingeworfen) {
				IOHandler.stopBallMotor();
				zustandBall = ZustaendeBall.BALL_VORHANDEN;
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
				}
				lastMilis = System.currentTimeMillis();
			}
			lastBallEingeworfen = IOHandler.ball_BallEingeworfen;
			break;
		case BALL_VORHANDEN:
			long currentMilis = System.currentTimeMillis();
			Globals.spielzeit += (currentMilis - lastMilis);
			lastMilis = currentMilis;
			if (IOHandler.spieler1_Tor) {
				Globals.toreSpieler2++;
				zustandBall = ZustaendeBall.KEIN_BALL;
			} else {
				if (IOHandler.spieler2_Tor) {
					Globals.toreSpieler1++;
					zustandBall = ZustaendeBall.KEIN_BALL;
				}
			}
			break;
		}
	}

	// Erstellen der IO Verbindungen
	public static void init() {
		IOHandler.init(false);
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
				Globals.ausparkzeit = System.currentTimeMillis();
				Globals.ausparken = false;
				Globals.mBotsBereit = false;
				zustandSpiel = ZustaendeSpiel.SPIEL_LAEUFT;
			}
			break;
		case SPIEL_LAEUFT:
			laufendesSpiel();
			if ((Globals.spielzeit > TimeUnit.MINUTES.toMillis(3) || Globals.toreSpieler1 + Globals.toreSpieler2 >= 5)
					&& zustandBall == ZustaendeBall.KEIN_BALL) {
				gameStopTime = System.currentTimeMillis();
				zustandSpiel = ZustaendeSpiel.SPIEL_FERTIG;
			}
			break;
		case SPIEL_FERTIG:
			if (((System.currentTimeMillis() - Globals.ausparkzeit) > Globals.maxAusparkzeit)
					|| ((System.currentTimeMillis() - gameStopTime) > Globals.maxWartezeit)) {
				zustandSpiel = ZustaendeSpiel.EINPARKEN;
			} else {
				if (IOHandler.spieler1_NeuerBall && IOHandler.spieler2_NeuerBall) {
					Globals.toreSpieler1 = 0;
					Globals.toreSpieler2 = 0;
					Globals.spielzeit = 0;
					zustandSpiel = ZustaendeSpiel.SPIEL_LAEUFT;
				}
			}
			break;
		case EINPARKEN:
			Globals.einparken = true;
			if (Globals.mBotsGeparkt) {
				Globals.einparken = false;
				Globals.mBotsGeparkt = false;
				Globals.toreSpieler1 = 0;
				Globals.toreSpieler2 = 0;
				Globals.spielzeit = 0;
				Globals.mBotSpieler1 = Colors.WHITE;
				Globals.mBotSpieler2 = Colors.WHITE;
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
