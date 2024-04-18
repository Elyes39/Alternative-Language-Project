import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainTest {
    @Test
    public void testMain() {
        // Redirect System.out to a ByteArrayOutputStream so we can capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Run the main method
        Main.main(new String[0]);

        // Check the output
        String output = outContent.toString();
        assertTrue(output.contains("Cell at index"));
        assertFalse(output.contains("-"));
        assertTrue(output.matches(".*\\d+\\.\\d+.*")); // Check if there's a float in the output
    }
}