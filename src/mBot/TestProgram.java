package mBot;

public class TestProgram {

    public static void main(String[] args) {

    	States state = States.DRIVE;
        int rSpeed = 100;
        int lSpeed = 200;
        MotorDirection direction = MotorDirection.FORWARD;
    	
        
        String command = "";
        command += (char) state.ordinal();
        if(rSpeed <= 127) {
        	command += (char) rSpeed;
        } else {
        	command += (char) ((rSpeed - 256) & 0xFF);
        }
        if (lSpeed <= 127) {
        	command += (char) lSpeed;
        } else {
        	command += (char) ((lSpeed - 256) & 0xFF);
        }
        command += (char) direction.ordinal();
        
        System.out.println(command);
    }

}
