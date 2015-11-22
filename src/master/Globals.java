package master;

import display.Colors;

public class Globals {
    
    public static boolean ausparken = false;
    public static boolean einparken = false;
    public static boolean mBotsBereit = false;
    public static boolean mBotsGeparkt = false;
    public static boolean spielLaeuft = false;
    public static long spielzeit = 0; //in ms
    public static int toreSpieler1 = 0;
    public static int toreSpieler2 = 0;
    public static Colors mBotSpieler1 = Colors.WHITE;
    public static Colors mBotSpieler2 = Colors.WHITE;
    
    public static boolean stop = false;
    
    public static double joystickMaxInc = 4096;

}
