package io;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class StepperMotorPinListener implements GpioPinListenerDigital {

	StepperMotor motor;
	int stepCount;

	StepperMotorPinListener(StepperMotor motor, int stepCount) {
		this.motor = motor;
		this.stepCount = stepCount;
	}

	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		if (event.getState().isLow()) {
			this.motor.stopMotor();
			this.motor.setPosition(stepCount);
		}
	}

}
