package mBot;

import java.util.concurrent.TimeUnit;

import io.IOHandler;

public class TestAnalogInput {

	static int deadzoneLPos = 32;
	static int deadzoneLNeg = 32;
	static int deadzoneRPos = 32;
	static int deadzoneRNeg = 32;
	static int minMotorSpeedLPos = 60;
	static int minMotorSpeedLNeg = 60;
	static int minMotorSpeedRPos = 60;
	static int minMotorSpeedRNeg = 60;

	public static void main(String[] args) {

		IOHandler.init(true);

		System.out.println("IO initialized!");

		MBot mBot = new MBot("000502031DD3");
		mBot.synch();
		boolean stop = false;
		long starttime = System.currentTimeMillis();
		while (!stop) {
			int lSpeed = MBotSteuerung.getSpeed(IOHandler.spieler1_JoystickL, deadzoneLPos, deadzoneLNeg, minMotorSpeedLPos, minMotorSpeedLNeg);
			int rSpeed = MBotSteuerung.getSpeed(IOHandler.spieler1_JoystickR, deadzoneRPos, deadzoneRNeg, minMotorSpeedRPos, minMotorSpeedRNeg);
			MotorDirection direction = MBotSteuerung.getDirection(IOHandler.spieler1_JoystickL, IOHandler.spieler1_JoystickR);
			
			System.out.println(lSpeed + " " + rSpeed + " " + direction);
			mBot.sendCommand(States.DRIVE, rSpeed, lSpeed, direction);
			try {
				TimeUnit.MILLISECONDS.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - starttime > 30000) {
				stop = true;
			}
		}
		mBot.sendCommand(States.STOP, 0, 0, MotorDirection.STOP);
		IOHandler.closeAll();

	}

}
