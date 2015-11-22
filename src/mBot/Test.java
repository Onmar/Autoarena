package mBot;

public class Test {

    public static void main(String[] args) {

        int Spieler1_mBotIndex;
        int Spieler2_mBotIndex;

        long[] mBot_StartTimes = new long[] { 4, 6, 3 };

        Spieler1_mBotIndex = 0;
        Spieler2_mBotIndex = 1;
        if (mBot_StartTimes[2] < mBot_StartTimes[0]) { // 2 laedt
                                                       // laenger als
                                                       // 0
            if (mBot_StartTimes[0] < mBot_StartTimes[1]) { // 0 laedt
                                                           // laenger
                                                           // als 1
                Spieler2_mBotIndex = 2;
            } else { // 1 laedt laenger als 0
                Spieler1_mBotIndex = 2;
            }
        } else { // 0 laedt laenger als 2
            if (mBot_StartTimes[2] < mBot_StartTimes[1]) { // 2 laedt
                                                           // laenger
                                                           // als 1
                Spieler2_mBotIndex = 2;
            }
        }

        System.out.println("1: " + Spieler1_mBotIndex + " 2: " + Spieler2_mBotIndex);

    }

}
