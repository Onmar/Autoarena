package mBot;

public class TestProgram {

    public static void main(String[] args) {

        States state = States.DRIVE;
        int rSpeed = 255;
        int lSpeed = 100;
        MotorDirection direction = MotorDirection.FORWARD;

        char[] command = new char[] { (char) state.ordinal(), (char) rSpeed, (char) lSpeed,
                (char) direction.ordinal() };

        for (int i = 0; i < command.length; i++) {
            System.out.print(command[i]);
        }

    }

}
