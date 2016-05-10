package io;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.gpio.extension.ads.ADS1115Pin;
import com.pi4j.gpio.extension.ads.ADS1x15GpioProvider.ProgrammableGainAmplifierValue;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;

public class IOHandler {

	// GPIO Handler
	private final static GpioController gpio = GpioFactory.getInstance();

	// SpielerControls
	public static final int joystickAmplitude = 1024;
	// Scaled values from -(joystickAmplitude/2) to (joystickAmplitude/2)
	public static int spieler1_JoystickL; 
	public static int spieler1_JoystickR;
	public static int spieler2_JoystickL;
	public static int spieler2_JoystickR;
	// Buttons on Joysticks
	public static boolean spieler1_NeuerBall;
	public static boolean spieler2_NeuerBall;
	// Sensors for goal
	public static boolean spieler1_Tor;
	public static boolean spieler2_Tor;
	
	// LadeBoxen
	// Robot at the door
	public static boolean ladeBoxen_SensorTor = false; 
	// Robot in box
	public static boolean[] ladeBoxen_Sensoren = new boolean[] { false, false, false }; 
	// Target Position
	private static LagerPosition ladeBoxen_SollPosition = LagerPosition.TOR;
	// Stepper Motor for Box
	private static StepperMotor ladeBoxen_Motor; 
	
	// BallRueckfuehrung
	public static boolean ball_BallEingeworfen;
	private final static GpioPinDigitalOutput ball_Motor = gpio.provisionDigitalOutputPin(IOList.ball_Motor,
			"ball_Motor", PinState.HIGH);

	// Variables End
	
	public static void init(boolean debug) {
		// I2C
		try {
			final ADS1115GpioProvider AnalogGpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1,
					IOList.analogModuleAddress);
			GpioPinAnalogInput AnalogInputs[] = {
					gpio.provisionAnalogInputPin(AnalogGpioProvider, ADS1115Pin.INPUT_A0, "Spieler1_JoystickL"),
					gpio.provisionAnalogInputPin(AnalogGpioProvider, ADS1115Pin.INPUT_A1, "Spieler1_JoystickR"),
					gpio.provisionAnalogInputPin(AnalogGpioProvider, ADS1115Pin.INPUT_A2, "Spieler2_JoystickL"),
					gpio.provisionAnalogInputPin(AnalogGpioProvider, ADS1115Pin.INPUT_A3, "Spieler2_JoystickR"), };
			AnalogGpioProvider.setProgrammableGainAmplifier(ProgrammableGainAmplifierValue.PGA_4_096V, ADS1115Pin.ALL);
			AnalogGpioProvider.setEventThreshold(20, ADS1115Pin.ALL);
			AnalogGpioProvider.setMonitorInterval(25);

			// spieler1_JoystickL
			AnalogInputs[0].addListener(new GpioPinListenerAnalog() {
				@Override
				public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
					if (event.getValue() > 0.0) {
						int value = (int) Math
								.round(((event.getValue() * 4.096) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE) / 3.4
										* joystickAmplitude)
								- (joystickAmplitude / 2);
						if (value > (joystickAmplitude / 2)) {
							value = (joystickAmplitude / 2) - 1;
						} else {
							if (value < -(joystickAmplitude / 2)) {
								value = -((joystickAmplitude / 2) - 1);
							}
						}
						IOHandler.spieler1_JoystickL = value;
					}
				}
			});
			// spieler1_JoystickR
			AnalogInputs[1].addListener(new GpioPinListenerAnalog() {
				@Override
				public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
					if (event.getValue() > 0.0) {
						int value = (int) Math
								.round(((event.getValue() * 4.096) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE) / 3.4
										* joystickAmplitude)
								- (joystickAmplitude / 2);
						if (value > (joystickAmplitude / 2)) {
							value = (joystickAmplitude / 2) - 1;
						} else {
							if (value < -(joystickAmplitude / 2)) {
								value = -((joystickAmplitude / 2) - 1);
							}
						}
						IOHandler.spieler1_JoystickR = value;
					}
				}
			});
			// spieler2_JoystickL
			AnalogInputs[2].addListener(new GpioPinListenerAnalog() {
				@Override
				public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
					if (event.getValue() > 0.0) {
						int value = (int) Math
								.round(((event.getValue() * 4.096) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE) / 3.4
										* joystickAmplitude)
								- (joystickAmplitude / 2);
						if (value > (joystickAmplitude / 2)) {
							value = (joystickAmplitude / 2) - 1;
						} else {
							if (value < -(joystickAmplitude / 2)) {
								value = -((joystickAmplitude / 2) - 1);
							}
						}
						IOHandler.spieler2_JoystickL = value;
					}
				}
			});
			// spieler2_JoystickR
			AnalogInputs[3].addListener(new GpioPinListenerAnalog() {
				@Override
				public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
					if (event.getValue() > 0.0) {
						int value = (int) Math
								.round(((event.getValue() * 4.096) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE) / 3.4
										* joystickAmplitude)
								- (joystickAmplitude / 2);
						if (value > (joystickAmplitude / 2)) {
							value = (joystickAmplitude / 2) - 1;
						} else {
							if (value < -(joystickAmplitude / 2)) {
								value = -((joystickAmplitude / 2) - 1);
							}
						}
						IOHandler.spieler2_JoystickR = value;
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		// GPIO

		// Inputs Start

		// spieler1_NeuerBall
		final GpioPinDigitalInput spieler1_NeuerBall = gpio.provisionDigitalInputPin(IOList.spieler1_NeuerBall,
				PinPullResistance.PULL_DOWN);
		spieler1_NeuerBall.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler1_NeuerBall = event.getState().isHigh();
			}
		});
		spieler1_NeuerBall.setDebounce(10);
		IOHandler.spieler1_NeuerBall = spieler1_NeuerBall.isHigh();

		// spieler2_NeuerBall
		final GpioPinDigitalInput spieler2_NeuerBall = gpio.provisionDigitalInputPin(IOList.spieler2_NeuerBall,
				PinPullResistance.PULL_DOWN);
		spieler2_NeuerBall.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler2_NeuerBall = event.getState().isHigh();
			}
		});
		spieler2_NeuerBall.setDebounce(10);
		IOHandler.spieler2_NeuerBall = spieler2_NeuerBall.isHigh();

		// spieler1_Tor
		final GpioPinDigitalInput spieler1_Tor = gpio.provisionDigitalInputPin(IOList.spieler1_Tor,
				PinPullResistance.PULL_DOWN);
		spieler1_Tor.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler1_Tor = event.getState().isHigh();
			}
		});
		spieler1_Tor.setDebounce(10);
		IOHandler.spieler1_Tor = spieler1_Tor.isHigh();

		// spieler2_Tor
		final GpioPinDigitalInput spieler2_Tor = gpio.provisionDigitalInputPin(IOList.spieler2_Tor,
				PinPullResistance.PULL_DOWN);
		spieler2_Tor.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler2_Tor = event.getState().isHigh();
			}
		});
		spieler2_Tor.setDebounce(10);
		IOHandler.spieler2_Tor = spieler2_Tor.isHigh();

		{
			// ladeBoxen_Sensoren
			GpioPinDigitalInput[] ladeBoxen_Sensoren = new GpioPinDigitalInput[3];

			// ladeBoxen_Sensoren[0]
			ladeBoxen_Sensoren[0] = gpio.provisionDigitalInputPin(IOList.ladeBox_Sensoren[0],
					PinPullResistance.PULL_DOWN);
			ladeBoxen_Sensoren[0].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_Sensoren[0] = event.getState().isHigh();
				}
			});
			ladeBoxen_Sensoren[0].setDebounce(10);

			// ladeBoxen_Sensoren[1]
			ladeBoxen_Sensoren[1] = gpio.provisionDigitalInputPin(IOList.ladeBox_Sensoren[1],
					PinPullResistance.PULL_DOWN);
			ladeBoxen_Sensoren[1].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_Sensoren[1] = event.getState().isHigh();
				}
			});
			ladeBoxen_Sensoren[1].setDebounce(10);

			// ladeBoxen_Sensoren[2]
			ladeBoxen_Sensoren[2] = gpio.provisionDigitalInputPin(IOList.ladeBox_Sensoren[2],
					PinPullResistance.PULL_DOWN);
			ladeBoxen_Sensoren[2].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_Sensoren[2] = event.getState().isHigh();
				}
			});
			ladeBoxen_Sensoren[2].setDebounce(10);

			// ladeBoxen_SensorTor
			final GpioPinDigitalInput ladeBoxen_SensorTor = gpio.provisionDigitalInputPin(IOList.ladeBox_SensorTor,
					PinPullResistance.PULL_DOWN);
			ladeBoxen_SensorTor.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_SensorTor = event.getState().isHigh();
				}
			});
			ladeBoxen_SensorTor.setDebounce(10);
		}

		// ball_BallEingeworfen
		final GpioPinDigitalInput ball_BallEingeworfen = gpio.provisionDigitalInputPin(IOList.ball_BallEingeworfen,
				PinPullResistance.PULL_DOWN);
		ball_BallEingeworfen.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.ball_BallEingeworfen = event.getState().isHigh();
			}
		});
		ball_BallEingeworfen.setDebounce(10);

		// Inputs Ende

		// Outputs Start

		// ladeBox_StepperMotor
		IOHandler.ladeBoxen_Motor = new StepperMotor(IOList.ladeBox_StepperMotor_Step, IOList.ladeBox_StepperMotor_Dir,
				IOList.ladeBox_StepperMotor_TerminalSwitch_low, null);
		IOHandler.ladeBoxen_Motor.setInterval(2);

		// ball_Motor
		ball_Motor.setShutdownOptions(true, PinState.LOW);

		// Outputs Ende
		// Send Motor to reference Point
		if (!debug) {
			IOHandler.ladeBoxen_Motor.toZero();
			IOHandler.ladeBoxen_Motor.moveToPosition(LagerPosition.TOR.position());
		}
	}

	public static void setLadeBoxSollPosition(LagerPosition newPosition) {
		if (newPosition != ladeBoxen_SollPosition && newPosition != LagerPosition.UNKNOWN) {
			while (!IOHandler.ladeBoxen_Motor.reachedPosition()) {
				try {
					TimeUnit.MILLISECONDS.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			ladeBoxen_SollPosition = newPosition;
			IOHandler.ladeBoxen_Motor.moveToPosition(newPosition.position());
		}
	}
	
	public static void setLadeBoxSollPosition(int newPosition) {
			while (!IOHandler.ladeBoxen_Motor.reachedPosition()) {
				try {
					TimeUnit.MILLISECONDS.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			IOHandler.ladeBoxen_Motor.moveToPosition(newPosition);
	}

	public static boolean positionReached() {
		return IOHandler.ladeBoxen_Motor.reachedPosition();
	}
	
	public static LagerPosition getLadeBoxSollPosition() {
		return ladeBoxen_SollPosition;
	}
	
	public static void startBallMotor() {
		ball_Motor.low();
	}
	
	public static void stopBallMotor() {
		ball_Motor.high();
	}

	public static void closeAll() {
		IOHandler.ladeBoxen_Motor.closeMotor();
		gpio.shutdown();
	}
}
