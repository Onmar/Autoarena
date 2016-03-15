package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Edgar Lederer <edgar.lederer@fhnw.ch>
 * @version 1.0.1
 * @since 2015-10-16
 */
public class ReadConsoleInput {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Liest einen String von der Konsole
     * <p>
     * Die Methode {@code readString()} liest einen String von der Konsole ein
     * und liefert diesen String zurück. Wird etwas anderes als ein String
     * eingegeben, so liefert die Methode einen leeren String zurück.
     * 
     * @param printError
     *            Gibt an, ob ein Error in der Konsole ausgegeben wird.
     * @return Der String der in der Konsole eingegeben wurde als String.
     */
    public static String readString(boolean printError) {
        try {
            return reader.readLine();
        } catch (Exception e) {
            if (printError)
                System.out.println("Error readString.");
            return "";
        }
    }

    /**
     * Liest eine ganze Zahl von der Konsole.
     * <p>
     * Die Methode {@code readInt()} liest eine ganze Zahl von der Konsole ein
     * und liefert die Zahl zurück. Wird etwas anderes als eine ganze Zahl
     * eingegeben, so liefert die Methode die kleinstmögliche darstellbare ganze
     * Zahl.
     * 
     * @param printError
     *            Gibt an, ob ein Error in der Konsole ausgegeben wird.
     * @return Die Ganze Zahl die in der Konsole eingegeben wurde als Integer.
     */
    public static int readInt(boolean printError) {
        try {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            if (printError)
                System.out.println("Error readInt.");
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Liest eine Gleitkommazahl von der Konsole.
     * <p>
     * Die Methode {@code readDouble()} liest eine Gelitkommazahl von der
     * Konsole ein und liefert diese Zahl zurück. Wird etwas anderes als eine
     * Gleitkommazahl eingegeben, so liefert die Methode NaN.
     * 
     * @param printError
     *            Gibt an, ob ein Error in der Konsole ausgegeben wird.
     * @return Die Gleitkommazahl die in der Konsole eingegeben wurde als
     *         Double.
     */
    public static double readDouble(boolean printError) {
        try {
            return Double.parseDouble(reader.readLine());
        } catch (Exception e) {
            if (printError)
                System.out.println("Error readDouble.");
            return Double.NaN;
        }
    }

    /**
     * Liest einen Boolschen Wert von der Konsole
     * <p>
     * Die Methode {@code readBoolean()} liest einen String von der Konsole ein
     * und liefert true zurück wenn der eingegebene String "y" oder "Y" ist.
     * Wird etwas anderes eingegeben wird false zurückgegeben.
     * Bei einem Fehler wird false zurückgegeben.
     * 
     * @param printError
     *            Gibt an, ob ein Error in der Konsole ausgegeben wird.
     * @return Ein Boolscher Wert.
     */
    public static boolean readBoolean(boolean printError) {
        try {
            String input = reader.readLine();
            if (input.equals("y") || input.equals("Y")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (printError) {
                System.out.println("Error readBoolean.");
            }
            return false;
        }
    }
}
