package io;

public class StepperMotorTest {

	public static void main(String[] args) {

		StepperMotor motor = new StepperMotor(IOList.ladeBox_StepperMotor_Step, IOList.ladeBox_StepperMotor_Dir, null, null);

		motor.setInterval(2);
		
		System.out.println("Start Move Forward");
		motor.moveSteps(1600, true);

		System.out.println("Start Move Backward");
		motor.moveSteps(1600, false);

		System.out.println("Done");
		motor.stopMotor();
		motor.closeMotor();
	}

}
