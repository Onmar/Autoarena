package master;

import java.util.concurrent.TimeUnit;

import mBot.MBot;
import mBot.MotorDirection;
import mBot.States;

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
			MotorDirection direction;
			int lSpeed = 0;
			int rSpeed = 0;
			if (IOHandler.spieler1_JoystickL > 0) {
				// Case: Linker Joystick nach vorn
				if (IOHandler.spieler1_JoystickL < deadzoneLPos) {
					// Case: Linker Joystick in positiver totzone
					lSpeed = 0;
				} else {
					// Case: Linker Joystick nicht in positiver totzone
					lSpeed = (int) Math
							.round(((double) (IOHandler.spieler1_JoystickL - deadzoneLPos))
									* (((double) 255 - minMotorSpeedLPos) / ((double) ((IOHandler.joystickAmplitude / 2) - 1) - deadzoneLPos)))
							+ minMotorSpeedLPos;
				}
				// should be IOHandler.spieler1_JoystickR
				if (IOHandler.spieler1_JoystickL > 0) {
					// Case: RechterJoystick nach vorn
					// should be IOHandler.spieler1_JoystickR
					if (IOHandler.spieler1_JoystickL < deadzoneRPos) {
						// Case: RechterJoystick in positiver totzone
						rSpeed = 0;
					} else {
						// Case: Rechter Joystick nicht in positiver totzone
						// should be IOHandler.spieler1_JoystickR
						rSpeed = (int) Math
								.round(((double) (IOHandler.spieler1_JoystickL - deadzoneRPos))
										* (((double) 255 - minMotorSpeedRPos) / ((double) ((IOHandler.joystickAmplitude / 2) - 1) - deadzoneRPos)))
								+ minMotorSpeedRPos;
					}
					direction = MotorDirection.FORWARD;
				} else {
					// Case: Rechter Joystick nach hinten
					// should be IOHandler.spieler1_JoystickR
					if (IOHandler.spieler1_JoystickL > -deadzoneRNeg) {
						// Case: Rechter Joystick in negativer totzone
						rSpeed = 0;
					} else {
						// Case: Rechter Joystick nicht in negativer totzone
						// should be IOHandler.spieler1_JoystickR
						rSpeed = (int) Math
								.round(((double) (-IOHandler.spieler1_JoystickL - deadzoneRNeg))
										* (((double) 255 - minMotorSpeedRNeg) / ((double) ((IOHandler.joystickAmplitude / 2) - 1) - deadzoneRNeg)))
								+ minMotorSpeedRNeg;
					}
					direction = MotorDirection.RIGHT;
				}
			} else {
				// Linker Joystick nach hinten
				if (-IOHandler.spieler1_JoystickL < deadzoneLNeg) {
					// Case: Linker Joystick in negativer totzone
					lSpeed = 0;
				} else {
					// Case: Linker Joystick nicht in negativer totzone
					lSpeed = (int) Math
							.round(((double) (-IOHandler.spieler1_JoystickL - deadzoneLNeg))
									* (((double) 255 - minMotorSpeedLNeg) / ((double) ((IOHandler.joystickAmplitude / 2) - 1) - deadzoneLNeg)))
							+ minMotorSpeedLNeg;
				}
				// should be IOHandler.spieler1_JoystickR
				if (IOHandler.spieler1_JoystickL > 0) {
					// Case: Rechter Joystick nach vorn
					// should be IOHandler.spieler1_JoystickR
					if (IOHandler.spieler1_JoystickL < deadzoneRPos) {
						// Case: Rechter Joystick in positiver totzone
						rSpeed = 0;
					} else {
						// Case: Rechter Joystick nicht in positiver totzone
						// should be IOHandler.spieler1_JoystickR
						rSpeed = (int) Math
								.round(((double) (IOHandler.spieler1_JoystickL - deadzoneRPos))
										* (((double) 255 - minMotorSpeedRPos) / ((double) ((IOHandler.joystickAmplitude / 2) - 1) - deadzoneRPos)))
								+ minMotorSpeedRPos;
					}
					direction = MotorDirection.LEFT;
				} else {
					// Case: Rechter Joystick nach hinten
					// should be IOHandler.spieler1_JoystickR
					if (IOHandler.spieler1_JoystickL > -deadzoneRNeg) {
						// Case: Rechter Joystick in negativer totzone
						rSpeed = 0;
					} else {
						// Case: Rechter Joystick nicht in negativer totzone
						// should be IOHandler.spieler1_JoystickR
						rSpeed = (int) Math
								.round(((double) (-IOHandler.spieler1_JoystickL - deadzoneRNeg))
										* (((double) 255 - minMotorSpeedRNeg) / ((double) ((IOHandler.joystickAmplitude / 2) - 1) - deadzoneRNeg)))
								+ minMotorSpeedRNeg;
					}
					direction = MotorDirection.BACKWARD;
				}
			}
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
