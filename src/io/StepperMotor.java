package io;

import java.util.concurrent.TimeUnit;

import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.*;

public class StepperMotor {

	private final GpioController gpio = GpioFactory.getInstance();

	private GpioStepperMotorComponent motor;
	private GpioPinDigitalInput terminalSwitch_low;
	private GpioPinDigitalInput terminalSwitch_high;

	private byte[] forward_step_sequence = new byte[] { (byte) 0b00, (byte) 0b01 };

	private byte[] backward_step_sequence = new byte[] { (byte) 0b10, (byte) 0b11 };

	private int currentPosition = Integer.MAX_VALUE;

	public StepperMotor(Pin step_Pin, Pin dir_Pin, Pin terminalSwitch_low_Pin, Pin terminalSwitch_high_Pin) {

		// Step Pin
		GpioPinDigitalOutput step = gpio.provisionDigitalOutputPin(step_Pin, "StepperMotor_Step", PinState.LOW);
		// Direction Pin
		GpioPinDigitalOutput dir = gpio.provisionDigitalOutputPin(dir_Pin, "StepperMotor_Dir", PinState.LOW);

		GpioPinDigitalOutput[] pins = new GpioPinDigitalOutput[] { step, dir };

		gpio.setShutdownOptions(true, PinState.LOW, pins);

		this.motor = new GpioStepperMotorComponent(pins);
		this.motor.setStepInterval(1);
		this.motor.setStepSequence(forward_step_sequence);

		// Low end terminal switch;
		if (terminalSwitch_low_Pin != null) {
			GpioPinDigitalInput terminalSwitch_low = gpio.provisionDigitalInputPin(terminalSwitch_low_Pin,
					PinPullResistance.PULL_DOWN);
			this.terminalSwitch_low = terminalSwitch_low;
			this.terminalSwitch_low.addListener(new StepperMotorPinListener(this, 0));
			this.terminalSwitch_low.setDebounce(5);
		}
		// High end terminal switch
		if (terminalSwitch_high_Pin != null) {
			GpioPinDigitalInput terminalSwitch_high = gpio.provisionDigitalInputPin(terminalSwitch_high_Pin,
					PinPullResistance.PULL_DOWN);
			this.terminalSwitch_high = terminalSwitch_high;
			this.terminalSwitch_high.addListener(new StepperMotorPinListener(this, Integer.MAX_VALUE));
			this.terminalSwitch_high.setDebounce(5);
		}
	}

	public void setInterval(long interval) {
		this.motor.setStepInterval(interval);
	}

	public void moveSteps(int steps, boolean forward) {
		if (forward) {
			this.motor.setStepSequence(forward_step_sequence);
		} else {
			this.motor.setStepSequence(backward_step_sequence);
		}
		this.motor.step(2 * steps);
	}

	public int getPosition() {
		return this.currentPosition;
	}

	public void setPosition(int position) {
		this.currentPosition = position;
	}

	public void moveToPosition(int position) {
		this.moveSteps(Math.abs(this.getPosition() - position), position > this.getPosition());
		this.currentPosition = position;
	}
	
	public void toZero() {
		this.motor.setStepSequence(backward_step_sequence);
		this.motor.setState(MotorState.FORWARD);
		while(!this.reachedPosition()) {
			try {
				TimeUnit.MILLISECONDS.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}

	public boolean reachedPosition() {
		return this.motor.getState() == MotorState.STOP;
	}

	public void stopMotor() {
		this.motor.setState(MotorState.STOP);
	}

	public void closeMotor() {
		gpio.shutdown();
	}

}
