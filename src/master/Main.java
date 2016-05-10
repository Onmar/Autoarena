package master;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import ablaufsteuerung.Ablaufsteuerung;
import display.Scoreboard;
import mBot.MBotSteuerung;

public class Main {

	public static void main(String[] args) {

		Ablaufsteuerung.init();
		MBotSteuerung.init();

		ActionListener ablaufsteuerung = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Ablaufsteuerung.run();
			}
		};
		Timer ablaufsteuerungTimer = new Timer(20, ablaufsteuerung);

		ActionListener mBotSteuerung = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MBotSteuerung.run();
			}
		};
		Timer mBotSteuerungTimer = new Timer(20, mBotSteuerung);

		Scoreboard window = new Scoreboard();
		window.setVisible(true);

		ablaufsteuerungTimer.start();
		mBotSteuerungTimer.start();

		while (!Globals.stop) {
		}

		ablaufsteuerungTimer.stop();
		mBotSteuerungTimer.stop();

		Ablaufsteuerung.close();
		MBotSteuerung.close();
		window.closeWindow();

	}

}
