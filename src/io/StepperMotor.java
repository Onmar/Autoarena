package io;

import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.*;

public class StepperMotor {

	private final GpioController gpio = GpioFactory.getInstance();

	private GpioStepperMotorComponent motor;

	private byte[] forward_step_sequence = new byte[] { (byte) 0b10,
			(byte) 0b11 };

	private byte[] backward_step_sequence = new byte[] { (byte) 0b00,
			(byte) 0b01 };

	public StepperMotor(GpioPinDigitalOutput step, GpioPinDigitalOutput dir) {

		GpioPinDigitalOutput[] pins = new GpioPinDigitalOutput[] { dir, step };

		gpio.setShutdownOptions(true, PinState.LOW, pins);

		this.motor = new GpioStepperMotorComponent(pins);
		this.motor.setStepInterval(2);
		this.motor.setStepSequence(forward_step_sequence);
		this.motor.setStepsPerRevolution(3200);
	}

	// speed in revolutions/min
	public void setSpeed(double speed) {
		this.motor.setStepInterval(Math.round(60000.0 / (speed * 800.0)));
	}

	public void moveSteps(int steps, boolean forward) {
		if (forward) {
			this.motor.setStepSequence(forward_step_sequence);
		} else {
			this.motor.setStepSequence(backward_step_sequence);
		}
		this.motor.step(2 * steps);
	}

	public void moveRevolutions(double revolutions, boolean forward) {
		if (forward) {
			this.motor.setStepSequence(forward_step_sequence);
		} else {
			this.motor.setStepSequence(backward_step_sequence);
		}
		this.motor.rotate(revolutions);
	}

	public boolean reachedPosition() {
		return this.motor.getState() == MotorState.STOP;
	}

	public void stopMotor() {
		this.motor.stop();
	}

	public void closeMotor() {
		gpio.shutdown();
	}

}
