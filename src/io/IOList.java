package io;

import com.pi4j.gpio.extension.ads.ADS1115GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class IOList {

	// Inputs Start
	// Analog
	public static final int analogModuleAddress = ADS1115GpioProvider.ADS1115_ADDRESS_0x48;
	// Digital
	public static final Pin spieler1_NeuerBall = RaspiPin.GPIO_26;
	public static final Pin spieler2_NeuerBall = RaspiPin.GPIO_23;
	public static final Pin spieler1_Tor = RaspiPin.GPIO_21;
	public static final Pin spieler2_Tor = RaspiPin.GPIO_22;
	public static final Pin[] ladeBox_Sensoren = new Pin[] { RaspiPin.GPIO_27, RaspiPin.GPIO_28, RaspiPin.GPIO_29 };
	public static final Pin ladeBox_SensorTor = RaspiPin.GPIO_11;
	public static final Pin ladeBox_StepperMotor_TerminalSwitch_low = RaspiPin.GPIO_03;
	public static final Pin ball_BallEingeworfen = RaspiPin.GPIO_25;
	// Inputs End
	
	// Outputs Start
	public static final Pin ball_Motor = RaspiPin.GPIO_24;
	public static final Pin ladeBox_StepperMotor_Step = RaspiPin.GPIO_00;
	public static final Pin ladeBox_StepperMotor_Dir = RaspiPin.GPIO_02;
	// Outputs End
}
