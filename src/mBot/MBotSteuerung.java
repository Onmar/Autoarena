package mBot;

import java.util.concurrent.TimeUnit;

import ablaufsteuerung.Ablaufsteuerung;
import ablaufsteuerung.ZustaendeSpiel;
import io.IOHandler;
import io.LagerPosition;
import master.Globals;

public class MBotSteuerung {

	// Array with mBot-Object References
	private static MBot[] mBots = new MBot[] { null, null, null };
	// Current State of FlowControl
	private static ZustaendeSteuerung zustand = ZustaendeSteuerung.STOP;

	// Array Index of mBot currently used by player 1.
	private static int Spieler1_mBotIndex = 0;
	// Array Index of mBot currently used by player 2.
	private static int Spieler2_mBotIndex = 0;

	private static long lastMillis = System.currentTimeMillis();

	public static ZustaendeSteuerung getMBotControlState() {
		return zustand;
	}

	// Gibt die skalierte Geschwindigkeit zurueck.
	public static int getSpeed(int joystickValue, int deadzonePos, int deadzoneNeg, int minMotorSpeedPos,
			int minMotorSpeedNeg) {
		int speed;
		if (joystickValue > 0) {
			// Case: Joystick nach vorn
			if (joystickValue < deadzonePos) {
				// Case: Joystick in positiver totzone
				speed = 0;
			} else {
				// Case: Joystick nicht in positiver totzone
				speed = (int) Math.round(((double) (joystickValue - deadzonePos)) * (((double) 255 - minMotorSpeedPos)
						/ ((double) ((IOHandler.joystickAmplitude / 2) - 1) - deadzonePos))) + minMotorSpeedPos;
			}
		} else {
			// Linker Joystick nach hinten
			if (-joystickValue < deadzoneNeg) {
				// Case: Linker Joystick in negativer totzone
				speed = 0;
			} else {
				// Case: Linker Joystick nicht in negativer totzone
				speed = (int) Math.round(((double) (-joystickValue - deadzoneNeg)) * (((double) 255 - minMotorSpeedNeg)
						/ ((double) ((IOHandler.joystickAmplitude / 2) - 1) - deadzoneNeg))) + minMotorSpeedNeg;
			}
		}
		return speed;
	}

	// Gibt die Bewegungsrichtung zurueck
	public static MotorDirection getDirection(int joystickValueL, int joystickValueR) {
		MotorDirection direction;
		if (joystickValueL > 0) {
			if (joystickValueR > 0) {
				direction = MotorDirection.FORWARD;
			} else {
				direction = MotorDirection.RIGHT;
			}
		} else {
			if (joystickValueR > 0) {
				direction = MotorDirection.LEFT;
			} else {
				direction = MotorDirection.BACKWARD;
			}
		}
		return direction;
	}

	// Stoppt alle mBots
	private static void stopAllMBots() {
		for (int i = 0; i < mBots.length; i++) {
			mBots[i].sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);
		}
	}

	// Sendet die Kommandos um die mBots einzuparken
	private static void einparken(int mBot1, int mBot2) {

		mBots[mBot1].synch();
		mBots[mBot2].synch();
		mBots[mBot1].sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);
		mBots[mBot2].sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);

		// LadeBox zu mBotBox von Spieler1
		IOHandler.setLadeBoxSollPosition(LagerPosition.values()[mBot1 + 1]);

		mBots[mBot2].synch();
		// mBot von Spieler2 weg von Linie fahren
		mBots[mBot2].sendCommand(MBotCommandStates.AVOID_LINE, 200, 200, MotorDirection.FORWARD);

		mBots[mBot1].synch();
		// mBot von Spieler1 soll Box suchen
		mBots[mBot1].sendCommand(MBotCommandStates.LINE_SEARCH, 200, 200, MotorDirection.FORWARD);
		// Warten bis mBot von Spieler1 bei Box ist
		while (!IOHandler.ladeBoxen_SensorTor) {
			try {
				TimeUnit.MILLISECONDS.sleep(20);
			} catch (InterruptedException e) {
			}
		}
		// mBot von Spieler1 einparkieren
		mBots[mBot1].sendCommand(MBotCommandStates.PARKING, 100, 100, MotorDirection.FORWARD);
		// Warten bis mBot von Spieler1 in Box ist
		while (!IOHandler.ladeBoxen_Sensoren[mBot1]) {
			try {
				TimeUnit.MILLISECONDS.sleep(20);
			} catch (InterruptedException e) {
			}
		}
		// mBot von Spieler1 stoppen
		mBots[mBot1].sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);
		mBots[mBot2].sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);

		// Lager zur mBotBox von Spieler2
		IOHandler.setLadeBoxSollPosition(LagerPosition.values()[mBot2 + 1]);

		mBots[mBot2].synch();
		// mBot von Spieler2 soll Box suchen
		mBots[mBot2].sendCommand(MBotCommandStates.LINE_SEARCH, 200, 200, MotorDirection.FORWARD);
		// Warten bis mBot von Spieler2 bei Box ist
		while (!IOHandler.ladeBoxen_SensorTor) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		// mBot von Spieler2 einparkieren
		mBots[mBot2].sendCommand(MBotCommandStates.PARKING, 100, 100, MotorDirection.FORWARD);
		// Warten bis mBot von Spieler2 in Box ist
		while (!IOHandler.ladeBoxen_Sensoren[mBot2]) {
			try {
				TimeUnit.MILLISECONDS.sleep(20);
			} catch (InterruptedException e) {
			}
		}
		// mBot von Spieler2 stoppen
		mBots[mBot2].sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);

		// Lager zum Tor
		IOHandler.setLadeBoxSollPosition(LagerPosition.TOR);
	}

	// Sendet die Kommandos um die mBots auszuparken
	private static void ausparken(int mBot1, int mBot2) {

		IOHandler.setLadeBoxSollPosition(LagerPosition.values()[mBot1 + 1]);

		mBots[mBot1].synch();
		mBots[mBot2].synch();
		mBots[mBot1].sendCommand(MBotCommandStates.GAME_START, 200, 200, MotorDirection.BACKWARD);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
		}
		mBots[mBot1].sendCommand(MBotCommandStates.GAME_START, 160, 160, MotorDirection.LEFT);
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
		}
		mBots[mBot1].sendCommand(MBotCommandStates.GAME_START, 200, 200, MotorDirection.BACKWARD);
		try {
			TimeUnit.MILLISECONDS.sleep(1500);
		} catch (InterruptedException e) {
		}
		mBots[mBot1].sendCommand(MBotCommandStates.GAME_START, 0, 0, MotorDirection.STOP);

		IOHandler.setLadeBoxSollPosition(LagerPosition.values()[mBot2 + 1]);

		mBots[mBot2].synch();
		mBots[mBot2].sendCommand(MBotCommandStates.GAME_START, 200, 200, MotorDirection.BACKWARD);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
		}
		mBots[mBot2].sendCommand(MBotCommandStates.GAME_START, 160, 160, MotorDirection.RIGHT);
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e) {
		}
		mBots[mBot2].sendCommand(MBotCommandStates.GAME_START, 200, 200, MotorDirection.BACKWARD);
		try {
			TimeUnit.MILLISECONDS.sleep(1500);
		} catch (InterruptedException e) {
		}
		mBots[mBot2].sendCommand(MBotCommandStates.GAME_START, 0, 0, MotorDirection.STOP);

		IOHandler.setLadeBoxSollPosition(LagerPosition.TOR);
	}

	// Sendet die Kommandos waehrend dem Spiel
	private static void spielSendCommands(int mBot1, int mBot2) {

		if (System.currentTimeMillis() - lastMillis > 1000) {
			lastMillis = System.currentTimeMillis();
			mBots[mBot1].synch();
			mBots[mBot2].synch();
		}

		// Speed of Left Motor for player 1
		int Sp1SpeedL = getSpeed(IOHandler.spieler1_JoystickL, 32, 32, 60, 60);
		// Speed of Right Motor for player 1
		int Sp1SpeedR = getSpeed(IOHandler.spieler1_JoystickR, 32, 32, 60, 60);
		// Motor Direction for player 1
		MotorDirection Sp1Dir = getDirection(IOHandler.spieler1_JoystickL, IOHandler.spieler1_JoystickR);

		// Send Command to mBot1
		mBots[mBot1].sendCommand(MBotCommandStates.DRIVE, Sp1SpeedL, Sp1SpeedR, Sp1Dir);

		// Speed of Left Motor for player 1
		int Sp2SpeedL = getSpeed(IOHandler.spieler2_JoystickL, 32, 32, 60, 60);
		// Speed of Right Motor for player 1
		int Sp2SpeedR = getSpeed(IOHandler.spieler2_JoystickR, 32, 32, 60, 60);
		// Motor Direction for player 1
		MotorDirection Sp2Dir = getDirection(IOHandler.spieler2_JoystickL, IOHandler.spieler2_JoystickR);

		// Send Command to mBot2
		mBots[mBot2].sendCommand(MBotCommandStates.DRIVE, Sp2SpeedL, Sp2SpeedR, Sp2Dir);
	}

	// Oeffnet alle Verbindungen zu den mBots
	public static void init() {
		for (int i = 0; i < mBots.length && i < Globals.mBotAddresses.length; i++) {
			mBots[i] = new MBot(Globals.mBotAddresses[i]);
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
			}
			mBots[i].synch();
			mBots[i].sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);
		}
	}

	// Statusmaschine
	public static void run() {

		// Zustandsmaschine fuer mBotSteuerung
		switch (zustand) {
		case STOP:
			if (Globals.ausparken) {
				zustand = ZustaendeSteuerung.AUSPARKEN;
			}
			break;
		case AUSPARKEN:
			// Waehlt die beiden mBots aus, welche am laengsten am laden
			// sind.
			switch (Globals.round % 3) {
			case 0:
				Spieler1_mBotIndex = 0;
				Spieler2_mBotIndex = 1;
				break;
			case 1:
				Spieler1_mBotIndex = 0;
				Spieler2_mBotIndex = 2;
				break;
			case 2:
				Spieler1_mBotIndex = 1;
				Spieler2_mBotIndex = 2;
				break;
			}
			Globals.mBotSpieler1 = display.Colors.values()[Spieler1_mBotIndex + 1];
			Globals.mBotSpieler2 = display.Colors.values()[Spieler2_mBotIndex + 1];
			ausparken(Spieler1_mBotIndex, Spieler2_mBotIndex);
			Globals.mBotsBereit = true;
			zustand = ZustaendeSteuerung.SPIEL;
			break;
		case SPIEL:
			if (Ablaufsteuerung.getGameState() == ZustaendeSpiel.SPIEL_LAEUFT) {
				spielSendCommands(Spieler1_mBotIndex, Spieler2_mBotIndex);
			} else {
				mBots[Spieler1_mBotIndex].sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);
				mBots[Spieler2_mBotIndex].sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);
			}
			if (Globals.einparken) {
				zustand = ZustaendeSteuerung.EINPARKEN;
			}
			break;
		case EINPARKEN:
			einparken(Spieler1_mBotIndex, Spieler2_mBotIndex);
			Globals.mBotsGeparkt = true;
			zustand = ZustaendeSteuerung.STOP;
			break;
		}
	}

	// Schliesst alle Verbindungen
	public static void close() {
		stopAllMBots();
		// Close References
		for (int i = 0; i < mBots.length; i++) {
			mBots[i].disconnect();
			mBots[i] = null;
		}
	}

}
