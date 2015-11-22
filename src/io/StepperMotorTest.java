package io;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;

public class StepperMotorTest {

	public static void main(String[] args) {

		final GpioController gpio = GpioFactory.getInstance();

		StepperMotor motor = new StepperMotor(
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24));

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
