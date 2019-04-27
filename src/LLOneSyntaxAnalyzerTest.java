import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LLOneSyntaxAnalyzerTest {
    private Compiler compiler2 = CompilerExample.compiler2;


    @Test
    void testCase1() {
        String s = "test/example3.c";
        try {
            compiler2.compile(s);
            assertEquals("Yes" + System.lineSeparator(),
                    FileUtils.readFileToString(new File("test/example3.syn"), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCase2() {
        String s = "test/example4.c";
        try {
            compiler2.compile(s);
            assertEquals("No" + System.lineSeparator(),
                    FileUtils.readFileToString(new File("test/example4.syn"), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}