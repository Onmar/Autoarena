package mBot;

import java.util.concurrent.TimeUnit;

import io.IOHandler;
import io.LagerPosition;
import master.Globals;

public class MBotSteuerung {

	private static boolean endProgram = false;

	/*
	 * Bluetooth Addresses of mBots: mBot1: 000502031DD3 mBot2: mBot3:
	 */

	// Bluetooth Addresses for mBots
	private static String[] bluetoothAddresses = new String[] { "000502031DD3", "Dummy", "Dummy" };
	// Array with mBot-Object References
	private static MBot[] mBots = new MBot[] { null, null, null };
	// Current State of FlowControl
	private static ZuständeSteuerung zustand = ZuständeSteuerung.STOP;

	// Array Index of mBot currently used by player 1.
	private static int Spieler1_mBotIndex = 0;
	// Array Index of mBot currently used by player 2.
	private static int Spieler2_mBotIndex = 0;
	// Last Time an mBot has started.
	private static long[] mBot_StartTimes = new long[bluetoothAddresses.length];
	
	private void einparken(int mBot1, int mBot2) {
		IOHandler.ladeBoxen_SollPosition = LagerPosition.values()[mBot1 + 1];
		mBots[mBot1].sendCommand(States.LINE_FOLLOW, 200, 200, MotorDirection.FORWARD);
		while(!IOHandler.ladeBoxen_Sensoren[mBot1]) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		IOHandler.ladeBoxen_SollPosition = LagerPosition.values()[mBot2 + 1];
		mBots[mBot2].sendCommand(States.LINE_FOLLOW, 200, 200, MotorDirection.FORWARD);
		while(!IOHandler.ladeBoxen_Sensoren[mBot2]) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private void ausparken(int mBot1, int mBot2) {
		IOHandler.ladeBoxen_SollPosition = LagerPosition.values()[mBot1 + 1];
		while (IOHandler.ladeBoxen_MomentanePosition != LagerPosition.values()[mBot1 + 1]) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		mBots[mBot1].sendCommand(States.DRIVE, 200, 200, MotorDirection.FORWARD);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
		}
		IOHandler.ladeBoxen_SollPosition = LagerPosition.values()[mBot2 + 1];
		mBots[mBot1].sendCommand(States.DRIVE, 100, 100, MotorDirection.RIGHT);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
		}
		mBots[mBot1].sendCommand(States.DRIVE, 200, 200, MotorDirection.FORWARD);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
		}
		mBots[mBot1].sendCommand(States.STOP, 0, 0, MotorDirection.STOP);
		while (IOHandler.ladeBoxen_MomentanePosition != LagerPosition.values()[mBot1 + 1]) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		mBots[mBot2].sendCommand(States.DRIVE, 200, 200, MotorDirection.FORWARD);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
		}
		mBots[mBot2].sendCommand(States.DRIVE, 100, 100, MotorDirection.LEFT);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
		}
		mBots[mBot2].sendCommand(States.DRIVE, 200, 200, MotorDirection.FORWARD);
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
		}
		mBots[mBot2].sendCommand(States.STOP, 0, 0, MotorDirection.STOP);
	}

	// Sends Commands to mBots while game is running
	public void spielSendCommands() {
		// Speed of Left Motor for player 1
		int Sp1SpeedL = Math.abs((int) Math.round(IOHandler.spieler1_JoystickL / Globals.joystickMaxInc * 255));
		// Speed of Right Motor for player 1
		int Sp1SpeedR = Math.abs((int) Math.round(IOHandler.spieler1_JoystickR / Globals.joystickMaxInc * 255));
		// Motor Direction for player 1
		MotorDirection Sp1Dir;
		if (Math.signum(IOHandler.spieler1_JoystickL) == 1.0) {
			if (Math.signum(IOHandler.spieler1_JoystickR) == 1.0) {
				Sp1Dir = MotorDirection.FORWARD;
			} else {
				Sp1Dir = MotorDirection.RIGHT;
			}
		} else {
			if (Math.signum(IOHandler.spieler1_JoystickR) == 1.0) {
				Sp1Dir = MotorDirection.LEFT;
			} else {
				Sp1Dir = MotorDirection.BACKWARD;
			}
		}

		// Speed of Left Motor for player 2
		int Sp2SpeedL = Math.abs((int) Math.round(IOHandler.spieler2_JoystickL / Globals.joystickMaxInc * 255));
		// Speed of Right Motor for player 2
		int Sp2SpeedR = Math.abs((int) Math.round(IOHandler.spieler2_JoystickR / Globals.joystickMaxInc * 255));
		// Motor Direction for player 2
		MotorDirection Sp2Dir;
		if (Math.signum(IOHandler.spieler2_JoystickL) == 1.0) {
			if (Math.signum(IOHandler.spieler2_JoystickR) == 1.0) {
				Sp2Dir = MotorDirection.FORWARD;
			} else {
				Sp2Dir = MotorDirection.RIGHT;
			}
		} else {
			if (Math.signum(IOHandler.spieler2_JoystickR) == 1.0) {
				Sp2Dir = MotorDirection.LEFT;
			} else {
				Sp2Dir = MotorDirection.BACKWARD;
			}
		}
		// Send Commands to mBots
		mBots[Spieler1_mBotIndex].sendCommand(States.DRIVE, Sp1SpeedR, Sp1SpeedL, Sp1Dir);
		mBots[Spieler2_mBotIndex].sendCommand(States.DRIVE, Sp2SpeedR, Sp2SpeedL, Sp2Dir);
	}

	public void run() {
		// Init
		for (int i = 0; i < mBots.length && i < bluetoothAddresses.length; i++) {
			mBots[i] = new MBot(bluetoothAddresses[i]);
			mBots[i].synch();
			mBots[i].sendCommand(States.STOP, 0, 0, MotorDirection.STOP);
		}

		// Main Loop
		while (!endProgram) {
			switch (zustand) {
			case STOP:
				if (Globals.ausparken) {
					zustand = ZuständeSteuerung.AUSPARKEN;
					Spieler1_mBotIndex = 0;
					Spieler2_mBotIndex = 1;
					if (mBot_StartTimes[2] < mBot_StartTimes[0]) {
						// 2 lädt länger als 0

						if (mBot_StartTimes[0] < mBot_StartTimes[1]) {
							// 0 lädt länger als 1
							Spieler2_mBotIndex = 2;
						} else {
							// 1 lädt länger als 0
							Spieler1_mBotIndex = 2;
						}
					} else {
						// 0 lädt länger als 2
						if (mBot_StartTimes[2] < mBot_StartTimes[1]) {
							// 2 lädt länger als 1
							Spieler2_mBotIndex = 2;
						}
					}
					mBot_StartTimes[Spieler1_mBotIndex] = System.currentTimeMillis();
					mBot_StartTimes[Spieler2_mBotIndex] = System.currentTimeMillis();
				}
				break;
			case AUSPARKEN:
				ausparken(Spieler1_mBotIndex, Spieler2_mBotIndex);
				zustand = ZuständeSteuerung.SPIEL;
				break;
			case SPIEL:
				spielSendCommands();
				if (Globals.einparken) {
					zustand = ZuständeSteuerung.EINPARKEN;
					mBots[Spieler1_mBotIndex].sendCommand(States.LINE_SEARCH, 200, 200, MotorDirection.FORWARD);
					mBots[Spieler1_mBotIndex].sendCommand(States.LINE_SEARCH, 200, 200, MotorDirection.FORWARD);
				}
				break;
			case EINPARKEN:
				einparken(Spieler1_mBotIndex, Spieler2_mBotIndex);
				Globals.mBotsGeparkt = true;
				zustand = ZuständeSteuerung.STOP;
				break;
			}
		}

		// Close References
		for(int i = 0 ; i < mBots.length; i++) {
			mBots[i].disconnect();
		}
		endProgram = false;
	}

	

}
