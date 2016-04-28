package io;

import com.pi4j.io.gpio.RaspiPin;

public class StepperMotorTest {

	public static void main(String[] args) {

		StepperMotor motor = new StepperMotor(RaspiPin.GPIO_25, RaspiPin.GPIO_24, null, null);

		System.out.println("Start Move Forward");
		motor.moveSteps(1600, true);
		while (!motor.reachedPosition()) {
		}
		System.out.println("Start Move Backward");
		motor.moveSteps(1600, false);
		while (!motor.reachedPosition()) {
		}
		System.out.println("Done");
		motor.stopMotor();
		motor.closeMotor();
	}

}
