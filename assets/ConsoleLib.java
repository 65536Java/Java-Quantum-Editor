package assets;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleLib {
    private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static PrintStream originalOut = System.out;
    private static PrintStream originalErr = System.err;

    public static void startCapture() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public static String getOutput() {
        return outContent.toString();
    }

    public static String getError() {
        return errContent.toString();
    }

    public static void stopCapture() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
