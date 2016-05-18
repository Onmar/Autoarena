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

		MBot mBot1 = new MBot("000502031DD3");
		MBot mBot2 = new MBot("000D190306E7");
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean stop = false;
		long starttime = System.currentTimeMillis();
		mBot1.synch();
		mBot2.synch();
		mBot1.sendCommand(MBotCommandStates.DRIVE, 0, 0, MotorDirection.STOP);
		mBot2.sendCommand(MBotCommandStates.DRIVE, 0, 0, MotorDirection.STOP);
		System.out.println("Go");
		while (!stop) {
			int lSpeed1 = MBotSteuerung.getSpeed(IOHandler.spieler1_JoystickL, deadzoneLPos, deadzoneLNeg, minMotorSpeedLPos, minMotorSpeedLNeg);
			int rSpeed1 = MBotSteuerung.getSpeed(IOHandler.spieler1_JoystickR, deadzoneRPos, deadzoneRNeg, minMotorSpeedRPos, minMotorSpeedRNeg);
			MotorDirection direction1 = MBotSteuerung.getDirection(IOHandler.spieler1_JoystickL, IOHandler.spieler1_JoystickR);
			mBot1.sendCommand(MBotCommandStates.DRIVE, lSpeed1, rSpeed1, direction1);
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int lSpeed2 = MBotSteuerung.getSpeed(IOHandler.spieler2_JoystickL, deadzoneLPos, deadzoneLNeg, minMotorSpeedLPos, minMotorSpeedLNeg);
			int rSpeed2 = MBotSteuerung.getSpeed(IOHandler.spieler2_JoystickR, deadzoneRPos, deadzoneRNeg, minMotorSpeedRPos, minMotorSpeedRNeg);
			MotorDirection direction2 = MBotSteuerung.getDirection(IOHandler.spieler2_JoystickL, IOHandler.spieler2_JoystickR);
			mBot2.sendCommand(MBotCommandStates.DRIVE, lSpeed2, rSpeed2, direction2);
			try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			
			}
			//System.out.println(lSpeed + " " + rSpeed + " " + direction);
			
			if((System.currentTimeMillis() - starttime) % 1000 < 15) {
				mBot1.synch();
				mBot2.synch();
			}
			if (System.currentTimeMillis() - starttime > 300000) {
				stop = true;
			}
		}
		mBot1.sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);
		mBot2.sendCommand(MBotCommandStates.STOP, 0, 0, MotorDirection.STOP);
		IOHandler.closeAll();

	}

}
