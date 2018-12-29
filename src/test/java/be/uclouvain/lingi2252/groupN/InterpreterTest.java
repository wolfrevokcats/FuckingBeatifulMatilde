package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.interpreter.Interpreter;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InterpreterTest {

    @Test
    public void welcomeTest() {
        Interpreter interpreter = Interpreter.getInstance();
        Map<String, String> subFeatures = interpreter.checkFeatures();
        String res = interpreter.welcome(subFeatures);
        assertTrue(res.contains("Type a number:\n"));

        for (String feature : subFeatures.keySet()) {
            assertTrue(res.contains(subFeatures.get(feature) + ". "));
            assertTrue(res.contains(feature + "\n"));
        }

        assertFalse(res.contains("contacts"));
        assertFalse(res.contains("thresholds"));
        assertFalse(res.contains("alarm"));
    }
}
