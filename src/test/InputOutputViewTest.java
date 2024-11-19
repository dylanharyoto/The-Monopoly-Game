import main.model.Property;
import main.view.InputOutputView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InputOutputViewTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture output for testing
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testDisplayMessage() {
        String message = "Hello, World!";
        InputOutputView.displayMessage(message);

        String output = outputStream.toString().trim();
        assertEquals(message, output);
    }

    @Test
    void testPromptInput_ValidOption() {
        InputOutputView.setInput("1");
        String result = InputOutputView.promptInput("Choose an option:", new String[]{"1", "2"});
        assertEquals("1", result);
    }

    @Test
    void testPromptInput_InvalidOption() {
        InputOutputView.setInput("3\n1");
        String result = InputOutputView.promptInput("Choose an option:", new String[]{"1", "2"});
        assertEquals("1", result); // Should return the valid option after the invalid one
    }

    @Test
    void testPromptFilename_ValidFilename() {
        InputOutputView.setInput("valid_filename");
        String result = InputOutputView.promptFilename("Enter a filename:");
        assertEquals("valid_filename", result);
    }

    @Test
    void testPromptFilename_InvalidFilename() {
        InputOutputView.setInput("invalid/filename\nvalid_filename");
        String result = InputOutputView.promptFilename("Enter a filename:");
        assertEquals("valid_filename", result); // Should return the valid filename after the invalid one
    }

    @Test
    void testDisplayAllProperties() {
        ArrayList<Property> properties = new ArrayList<>();
        properties.add(new Property(1, "P1", "Park Place", 350, 50));
        properties.add(new Property(2, "P2", "Boardwalk", 400, 60));

        InputOutputView.displayAllProperties(properties);

        String output = outputStream.toString().trim();
        String expectedOutput = "0. Park Place\n|---Price: 350 | Rent: 50\n" +
                "1. Boardwalk\n|---Price: 400 | Rent: 60";
        assertTrue(output.contains(expectedOutput));
    }

    @Test
    void testDisplayProperty() {
        Property property = new Property(1, "P1", "Park Place", 350, 50);
        InputOutputView.displayProperty(property);

        String output = outputStream.toString().trim();
        String expectedOutput = "The property's name is Park Place, price is 350, and rent is 50";
        assertEquals(expectedOutput, output);
    }

    @Test
    void testPromptString_ValidInput() {
        InputOutputView.setInput("ValidName");
        String result = InputOutputView.promptString("Enter a name:");
        assertEquals("ValidName", result);
    }

    @Test
    void testPromptString_InvalidInput() {
        InputOutputView.setInput("Invalid Name\nAnother Invalid@Name\nValidName");
        String result = InputOutputView.promptString("Enter a name:");
        assertEquals("ValidName", result); // Should return the valid name after invalid entries
    }

    @Test
    void testPromptInteger_ValidInput() {
        InputOutputView.setInput("25");
        int result = InputOutputView.promptInteger("Enter a positive integer:");
        assertEquals(25, result);
    }

    @Test
    void testPromptInteger_InvalidInput() {
        InputOutputView.setInput("abc\n-5\n0\n30");
        int result = InputOutputView.promptInteger("Enter a positive integer:");
        assertEquals(30, result); // Should return the valid integer after invalid entries
    }
}