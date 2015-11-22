package master;

import ablaufsteuerung.Ablaufsteuerung;
import display.Scoreboard;
import mBot.MBotSteuerung;

public class Main {

	private static boolean endProgram = false;

	public static void main(String[] args) {

		Thread ablaufsteuerung = new Thread() {
			@Override
			public void run() {
				Ablaufsteuerung.init();
				while (!endProgram) {
					Ablaufsteuerung.run();
				}
				Ablaufsteuerung.close();
			}
		};

		Thread mBotSteuerung = new Thread() {
			@Override
			public void run() {
				MBotSteuerung.init();
				while (!endProgram) {
					MBotSteuerung.run();
				}
				MBotSteuerung.close();
			}
		};

		Thread scoreboard = new Thread() {
			@Override
			public void run() {
				Scoreboard window = new Scoreboard();
				window.setVisible(true);
				while (!endProgram) {
				}
				window.closeWindow();
			}
		};

		ablaufsteuerung.start();
		mBotSteuerung.start();
		scoreboard.start();

		while (!Globals.stop) {
		}
		endProgram = true;

		try {
			ablaufsteuerung.join();
			mBotSteuerung.join();
			scoreboard.join();
		} catch (InterruptedException e) {
		}

	}

}
