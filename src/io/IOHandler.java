package io;

import java.io.IOException;
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

	private final static GpioController gpio = GpioFactory.getInstance(); // GPIO
																			// Handler

	private static boolean endProgram = false;

	// General
	public static boolean general_SpielStarten;
	// SpielerControls
	public static double spieler1_JoystickL;
	public static double spieler1_JoystickR;
	public static double spieler2_JoystickL;
	public static double spieler2_JoystickR;
	public static boolean spieler1_NeuerBall;
	public static boolean spieler2_NeuerBall;
	public static boolean spieler1_Tor;
	public static boolean spieler2_Tor;
	// LadeBoxen
	public static boolean ladeBoxen_MotorL; // To the Left in - Direction
	public static boolean ladeBoxen_MotorR; // To the Right in + Direction
	public static boolean[] ladeBoxen_Sensoren = new boolean[] { false, false, false };
	public static LagerPosition ladeBoxen_SollPosition = LagerPosition.TOR;
	public static LagerPosition ladeBoxen_MomentanePosition = LagerPosition.UNKNOWN;
	public static LagerPosition ladeBoxen_LastPosition = LagerPosition.UNKNOWN;
	private final static GpioPinDigitalOutput ladeBoxen_MotorL_Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,
			"ladeBoxen_MotorL", PinState.LOW);
	private final static GpioPinDigitalOutput ladeBoxen_MotorR_Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,
			"ladeBoxen_MotorR", PinState.LOW);
	// BallRückführung
	public static boolean ball_Motor;
	private final static GpioPinDigitalOutput ball_Motor_Pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,
			"ball_Motor", PinState.LOW);
	public static boolean ball_BallEingeworfen;

	public static void stop() {
		endProgram = true;
	}

	public static void init() throws IOException {
		// I2C
		final ADS1115GpioProvider gpioProvider = new ADS1115GpioProvider(I2CBus.BUS_1,
				ADS1115GpioProvider.ADS1115_ADDRESS_0x48);
		GpioPinAnalogInput myInputs[] = {
				gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A0, "MyAnalogInput-A0"),
				gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A1, "MyAnalogInput-A1"),
				gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A2, "MyAnalogInput-A2"),
				gpio.provisionAnalogInputPin(gpioProvider, ADS1115Pin.INPUT_A3, "MyAnalogInput-A3"), };
		gpioProvider.setProgrammableGainAmplifier(ProgrammableGainAmplifierValue.PGA_6_144V, ADS1115Pin.ALL);
		gpioProvider.setEventThreshold(500, ADS1115Pin.ALL);
		gpioProvider.setMonitorInterval(50);
		GpioPinListenerAnalog listenerAnalog = new GpioPinListenerAnalog() {
			@Override
			public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event) {
				int value = (int) Math.round(((event.getValue() * 255) / ADS1115GpioProvider.ADS1115_RANGE_MAX_VALUE));
				switch (event.getPin().getName()) {
				case "MyAnalogInput-A0":
					IOHandler.spieler1_JoystickL = value;
					break;
				case "MyAnalogInput-A1":
					IOHandler.spieler1_JoystickR = value;
					break;
				case "MyAnalogInput-A2":
					IOHandler.spieler2_JoystickL = value;
					break;
				case "MyAnalogInput-A3":
					IOHandler.spieler2_JoystickR = value;
					break;
				}
			}
		};
		myInputs[0].addListener(listenerAnalog);
		myInputs[1].addListener(listenerAnalog);
		myInputs[2].addListener(listenerAnalog);
		myInputs[3].addListener(listenerAnalog);

		// GPIO

		// Inputs Start

		// spieler1_NeuerBall
		final GpioPinDigitalInput spieler1_NeuerBall = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				PinPullResistance.PULL_DOWN);
		spieler1_NeuerBall.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler1_NeuerBall = event.getState().isHigh();
			}
		});

		// spieler2_NeuerBall
		final GpioPinDigitalInput spieler2_NeuerBall = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				PinPullResistance.PULL_DOWN);
		spieler2_NeuerBall.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler2_NeuerBall = event.getState().isHigh();
			}
		});

		// spieler1_Tor
		final GpioPinDigitalInput spieler1_Tor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				PinPullResistance.PULL_DOWN);
		spieler1_Tor.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler1_Tor = event.getState().isHigh();
			}
		});

		// spieler2_Tor
		final GpioPinDigitalInput spieler2_Tor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				PinPullResistance.PULL_DOWN);
		spieler2_Tor.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.spieler2_Tor = event.getState().isHigh();
			}
		});

		{
			// ladeBoxen_Sensoren
			final GpioPinDigitalInput[] ladeBoxen_Sensoren = new GpioPinDigitalInput[3];

			// ladeBoxen_Sensoren[0]
			ladeBoxen_Sensoren[0] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
			ladeBoxen_Sensoren[0].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_Sensoren[0] = event.getState().isHigh();
				}
			});

			// ladeBoxen_Sensoren[1]
			ladeBoxen_Sensoren[1] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
			ladeBoxen_Sensoren[1].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_Sensoren[1] = event.getState().isHigh();
				}
			});

			// ladeBoxen_Sensoren[2]
			ladeBoxen_Sensoren[2] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
			ladeBoxen_Sensoren[2].addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
					IOHandler.ladeBoxen_Sensoren[2] = event.getState().isHigh();
				}
			});
		}

		{
			// ladeBoxen_Positionsschalter
			final GpioPinDigitalInput[] ladeBoxen_Positionsschalter = new GpioPinDigitalInput[4];

			// ladeBoxen_Positionsschalter[0] (mBot Box 1)
			ladeBoxen_Positionsschalter[0] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
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
					if (ladeBoxen_SollPosition == ladeBoxen_MomentanePosition) {
						IOHandler.ladeBoxen_MotorL_Pin.low();
						IOHandler.ladeBoxen_MotorR_Pin.low();
					}
				}
			});

			// ladeBoxen_Positionsschalter[1] (mBot Box 2)
			ladeBoxen_Positionsschalter[1] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
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

			// ladeBoxen_Positionsschalter[2] (mBot Box 3)
			ladeBoxen_Positionsschalter[2] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
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

			// ladeBoxen_Positionsschalter[3] (Tor)
			ladeBoxen_Positionsschalter[3] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
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
		}

		// ball_BallEingeworfen
		final GpioPinDigitalInput ball_BallEingeworfen = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				PinPullResistance.PULL_DOWN);
		ball_BallEingeworfen.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				IOHandler.ball_BallEingeworfen = event.getState().isHigh();
			}
		});

		// Inputs Ende

		// Outputs Start

		// ladeBoxen_MotorL
		ladeBoxen_MotorL_Pin.setShutdownOptions(true, PinState.LOW);

		// ladeBoxen_MotorL
		ladeBoxen_MotorR_Pin.setShutdownOptions(true, PinState.LOW);

		// ball_Motor
		ball_Motor_Pin.setShutdownOptions(true, PinState.LOW);

		// Outputs Ende

		// LadeBox Position nach links bis ein Positionsschalter betätigt wird.
		ladeBoxen_MotorL_Pin.high();
		while (ladeBoxen_MomentanePosition == LagerPosition.UNKNOWN) {
		}
		ladeBoxen_MotorL_Pin.low();
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
}
