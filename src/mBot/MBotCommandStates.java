package mBot;

public enum MBotCommandStates {
    
    STOP(0),
    DRIVE(1),
    LINE_SEARCH(2),
    LINE_FOLLOW(3),
    PARKING(4),
    AVOID_LINE(5),
    GAME_START(6);
    
    private MBotCommandStates(int i) {
        //Do something with i
    }

}
