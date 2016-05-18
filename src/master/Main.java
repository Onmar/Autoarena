package master;

import java.util.concurrent.TimeUnit;

import ablaufsteuerung.Ablaufsteuerung;
import display.Scoreboard;
import mBot.MBotSteuerung;

public class Main {

	public static void main(String[] args) {

		Thread scoreboard = new Thread() {
			public void run() {
				Scoreboard window = new Scoreboard();
				window.setVisible(true);
				while (!Globals.stop) {
				}
			}
		};

		Thread ablaufsteuerung = new Thread() {
			public void run() {
				Ablaufsteuerung.init();
				while(!Globals.stop) {
					Ablaufsteuerung.run();
					try {
						TimeUnit.MILLISECONDS.sleep(20);
					} catch (InterruptedException e) {
					}
				}
				Ablaufsteuerung.close();
				
			}
		};

		Thread mBotSteuerung = new Thread() {
			public void run() {
				MBotSteuerung.init();
				while(!Globals.stop) {
					MBotSteuerung.run();
					try {
						TimeUnit.MILLISECONDS.sleep(20);
					} catch (InterruptedException e) {
					}
				}
				MBotSteuerung.close();
			}
		};

		scoreboard.start();
		ablaufsteuerung.start();
		mBotSteuerung.start();

		while (!Globals.stop) {
		}

	}

}
