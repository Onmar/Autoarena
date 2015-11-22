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
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;

public class IOHandler {

	// GPIO
	// Handler
	private final static GpioController gpio = GpioFactory.getInstance();
	// Outputs
	private final static GpioPinDigitalOutput ladeBoxen_MotorL_Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,
			"ladeBoxen_MotorL", PinState.HIGH);
	private final static GpioPinDigitalOutput ladeBoxen_MotorR_Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02,
			"ladeBoxen_MotorR", PinState.HIGH);
	private final static GpioPinDigitalOutput ball_Motor_Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24,
			"ball_Motor", PinState.HIGH);

	// General
	public static boolean general_SpielStarten;
	// SpielerControls
	public static int spieler1_JoystickL;
	public static int spieler1_JoystickR;
	public static int spieler2_JoystickL;
	public static int spieler2_JoystickR;
	public static int joystickAmplitude = 1024;
	public static boolean spieler1_NeuerBall;
	public static boolean spieler2_NeuerBall;
	public static boolean spieler1_Tor;
	public static boolean spieler2_Tor;
	// LadeBoxen
	public static boolean ladeBoxen_MotorL; // To the Left in - Direction
	public static boolean ladeBoxen_MotorR; // To the Right in + Direction
	public static boolean ladeBoxen_SensorTor = false; // Robot at the door
	public static boolean[] ladeBoxen_Sensoren = new boolean[] { false, false, false }; // Robot in Box

	private static LagerPosition ladeBoxen_SollPosition = LagerPosition.TOR;
	private static LagerPosition ladeBoxen_MomentanePosition = LagerPosition.UNKNOWN;
	private static LagerPosition ladeBoxen_LastPosition = LagerPosition.UNKNOWN;
	// BallRueckfuehrung
	public static boolean ball_Motor;
	public static boolean ball_BallEingeworfen;

	public static void init(boolean debug) {
		// I2C
		try {
			final ADS1115GpioProvider AnalogGpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1,
					ADS1115GpioProvider.ADS1115_ADDRESS_0x48);
			GpioPinAnalogInput AnalogInputs[] = {
					gpio.provisionAnalogInputPin(AnalogGpioProvider, ADS1115Pin.INPUT_A0, "Spieler1_JoystickL"),
					gpio.provisionAnalogInputPin(AnalogGpioProvider, ADS1115Pin.INPUT_A1, "Spieler1_JoystickR"),
					gpio.provisionAnalogInputPin(AnalogGpioProvider, ADS1115Pin.INPUT_A2, "Spieler2_JoystickL"),
					gpio.provisionAnalogInputPin(AnalogGpioProvider, ADS1115Pin.INPUT_A3, "Spieler2_JoystickR"), };
			AnalogGpioProvider.setProgrammableGainAmplifier(ProgrammableGainAmplifierValue.PGA_4_096V, ADS1115Pin.ALL);
			AnalogGpioProvider.setEventThreshold(20, ADS1115Pin.ALL);
			AnalogGpioProvider.setMonitorInterval(50);

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
		}
		// GPIO

		// Inputs Start

		// spieler1_NeuerBall
		final GpioPinDigitalInput spieler1_NeuerBall = gpio.provisionDigitalInputPin(RaspiPin.GPIO_26,
				PinPullResistance.PULL_DOWN);
		spieler1_NeuerBall.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler1_NeuerBall = event.getState().isHigh();
			}
		});
		spieler1_NeuerBall.setDebounce(50);

		// spieler2_NeuerBall
		final GpioPinDigitalInput spieler2_NeuerBall = gpio.provisionDigitalInputPin(RaspiPin.GPIO_23,
				PinPullResistance.PULL_DOWN);
		spieler2_NeuerBall.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler2_NeuerBall = event.getState().isHigh();
			}
		});
		spieler2_NeuerBall.setDebounce(50);

		// spieler1_Tor
		final GpioPinDigitalInput spieler1_Tor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21,
				PinPullResistance.PULL_DOWN);
		spieler1_Tor.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler1_Tor = event.getState().isHigh();
			}
		});
		spieler1_Tor.setDebounce(50);

		// spieler2_Tor
		final GpioPinDigitalInput spieler2_Tor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22,
				PinPullResistance.PULL_DOWN);
		spieler2_Tor.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler2_Tor = event.getState().isHigh();
			}
		});
		spieler2_Tor.setDebounce(50);

		{
			// ladeBoxen_Sensoren
			final GpioPinDigitalInput[] ladeBoxen_Sensoren = new GpioPinDigitalInput[3];

			// ladeBoxen_Sensoren[0]
			ladeBoxen_Sensoren[0] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_27, PinPullResistance.PULL_DOWN);
			ladeBoxen_Sensoren[0].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_Sensoren[0] = event.getState().isHigh();
				}
			});
			ladeBoxen_Sensoren[0].setDebounce(50);

			// ladeBoxen_Sensoren[1]
			ladeBoxen_Sensoren[1] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, PinPullResistance.PULL_DOWN);
			ladeBoxen_Sensoren[1].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_Sensoren[1] = event.getState().isHigh();
				}
			});
			ladeBoxen_Sensoren[1].setDebounce(50);

			// ladeBoxen_Sensoren[2]
			ladeBoxen_Sensoren[2] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_DOWN);
			ladeBoxen_Sensoren[2].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_Sensoren[2] = event.getState().isHigh();
				}
			});
			ladeBoxen_Sensoren[2].setDebounce(50);
			
			// ladeBoxen_SensorTor
			final GpioPinDigitalInput ladeBoxen_SensorTor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_11, PinPullResistance.PULL_DOWN);
			ladeBoxen_SensorTor.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_SensorTor = event.getState().isHigh();
				}
			});
			ladeBoxen_SensorTor.setDebounce(50);
		}

		{
			// ladeBoxen_Positionsschalter
			final GpioPinDigitalInput[] ladeBoxen_Positionsschalter = new GpioPinDigitalInput[4];

			// ladeBoxen_Positionsschalter[0] (mBot Box 1)
			ladeBoxen_Positionsschalter[0] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_12,
					PinPullResistance.PULL_DOWN);
			ladeBoxen_Positionsschalter[0].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					if (event.getState().isHigh()) {
						IOHandler.ladeBoxen_MomentanePosition = LagerPosition.M_BOT1;
					} else {
						IOHandler.ladeBoxen_LastPosition = LagerPosition.M_BOT1;
						IOHandler.ladeBoxen_MomentanePosition = LagerPosition.UNKNOWN;
					}
					if (ladeBoxen_SollPosition == LagerPosition.M_BOT1) {
						IOHandler.ladeBoxen_MotorL_Pin.low();
						IOHandler.ladeBoxen_MotorR_Pin.low();
					}
				}
			});
			ladeBoxen_Positionsschalter[0].setDebounce(50);

			// ladeBoxen_Positionsschalter[1] (mBot Box 2)
			ladeBoxen_Positionsschalter[1] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_13,
					PinPullResistance.PULL_DOWN);
			ladeBoxen_Positionsschalter[1].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					if (event.getState().isHigh()) {
						IOHandler.ladeBoxen_MomentanePosition = LagerPosition.M_BOT2;
					} else {
						IOHandler.ladeBoxen_LastPosition = LagerPosition.M_BOT2;
						IOHandler.ladeBoxen_MomentanePosition = LagerPosition.UNKNOWN;
					}
					if (ladeBoxen_SollPosition == ladeBoxen_MomentanePosition) {
						IOHandler.ladeBoxen_MotorL_Pin.low();
						IOHandler.ladeBoxen_MotorR_Pin.low();
					}
				}
			});
			ladeBoxen_Positionsschalter[1].setDebounce(50);

			// ladeBoxen_Positionsschalter[2] (mBot Box 3)
			ladeBoxen_Positionsschalter[2] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_14,
					PinPullResistance.PULL_DOWN);
			ladeBoxen_Positionsschalter[2].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					if (event.getState().isHigh()) {
						IOHandler.ladeBoxen_MomentanePosition = LagerPosition.M_BOT3;
					} else {
						IOHandler.ladeBoxen_LastPosition = LagerPosition.M_BOT3;
						IOHandler.ladeBoxen_MomentanePosition = LagerPosition.UNKNOWN;
					}
					if (ladeBoxen_SollPosition == ladeBoxen_MomentanePosition) {
						IOHandler.ladeBoxen_MotorL_Pin.low();
						IOHandler.ladeBoxen_MotorR_Pin.low();
					}
				}
			});
			ladeBoxen_Positionsschalter[2].setDebounce(50);

			// ladeBoxen_Positionsschalter[3] (Tor)
			ladeBoxen_Positionsschalter[3] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03,
					PinPullResistance.PULL_DOWN);
			ladeBoxen_Positionsschalter[3].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					if (event.getState().isHigh()) {
						IOHandler.ladeBoxen_MomentanePosition = LagerPosition.TOR;
					} else {
						IOHandler.ladeBoxen_LastPosition = LagerPosition.TOR;
						IOHandler.ladeBoxen_MomentanePosition = LagerPosition.UNKNOWN;
					}
					if (ladeBoxen_SollPosition == ladeBoxen_MomentanePosition) {
						IOHandler.ladeBoxen_MotorL_Pin.low();
						IOHandler.ladeBoxen_MotorR_Pin.low();
					}
				}
			});
			ladeBoxen_Positionsschalter[3].setDebounce(50);
		}

		// ball_BallEingeworfen
		final GpioPinDigitalInput ball_BallEingeworfen = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25,
				PinPullResistance.PULL_DOWN);
		ball_BallEingeworfen.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.ball_BallEingeworfen = event.getState().isHigh();
			}
		});
		ball_BallEingeworfen.setDebounce(50);

		// Inputs Ende

		// Outputs Start

		// ladeBoxen_MotorL
		ladeBoxen_MotorL_Pin.setShutdownOptions(true, PinState.LOW);

		// ladeBoxen_MotorL
		ladeBoxen_MotorR_Pin.setShutdownOptions(true, PinState.LOW);

		// ball_Motor
		ball_Motor_Pin.setShutdownOptions(true, PinState.LOW);

		// Outputs Ende

		if (!debug) {
			// LadeBox Position nach links bis ein Positionsschalter betaetigt
			// wird.
			ladeBoxen_MotorL_Pin.high();
			while (ladeBoxen_MomentanePosition == LagerPosition.UNKNOWN) {
				try {
					TimeUnit.MILLISECONDS.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			ladeBoxen_MotorL_Pin.low();
		}
	}

	public static void setLadeBoxSollPosition(LagerPosition newPosition) {
		ladeBoxen_SollPosition = newPosition;
		if (newPosition != ladeBoxen_MomentanePosition && newPosition != LagerPosition.UNKNOWN) {
			if (ladeBoxen_MomentanePosition != LagerPosition.UNKNOWN)
				if (ladeBoxen_SollPosition.ordinal() < ladeBoxen_MomentanePosition.ordinal()) {
					// To the Left
					ladeBoxen_MotorL_Pin.high();
					ladeBoxen_MotorR_Pin.low();
				} else {
					// To the Right
					ladeBoxen_MotorL_Pin.low();
					ladeBoxen_MotorR_Pin.high();
				}
		} else {
			if (ladeBoxen_SollPosition.ordinal() < ladeBoxen_LastPosition.ordinal()) {
				// To the Left
				ladeBoxen_MotorL_Pin.high();
				ladeBoxen_MotorR_Pin.low();
			} else {
				if (ladeBoxen_SollPosition.ordinal() > ladeBoxen_LastPosition.ordinal()) {
					// To the Right
					ladeBoxen_MotorL_Pin.low();
					ladeBoxen_MotorR_Pin.high();
				} else {
					// ladeBoxen_SollPosition == ladeBoxen_LastPosition
					if (ladeBoxen_MotorL_Pin.isHigh()) {
						ladeBoxen_MotorL_Pin.low();
						ladeBoxen_MotorR_Pin.high();
					} else {
						ladeBoxen_MotorL_Pin.high();
						ladeBoxen_MotorR_Pin.low();
					}
				}
			}
		}
	}

	public static boolean positionReached() {
		return ladeBoxen_SollPosition == ladeBoxen_MomentanePosition;
	}

	public static void closeAll() {

		gpio.shutdown();

	}
}
