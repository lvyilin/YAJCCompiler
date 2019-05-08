import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OPGSyntaxAnalyzerTest {

    private Compiler compiler = CompilerInstance.compiler3;


    @Test
    void testCase1() {
        String s = "test/example7.c";
        try {
            compiler.compile(s);
            assertEquals("Yes" + System.lineSeparator(),
                    FileUtils.readFileToString(new File("test/example7.syn"), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCase2() {
        String s = "test/example0.c";
        try {
            compiler.compile(s);
            assertEquals("Yes" + System.lineSeparator(),
                    FileUtils.readFileToString(new File("test/example0.syn"), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCase3() {
        String s = "test/example8.c";
        try {
            compiler.compile(s);
            assertEquals("No" + System.lineSeparator(),
                    FileUtils.readFileToString(new File("test/example8.syn"), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCase4() {
        String s = "test/example9.c";
        try {
            compiler.compile(s);
            assertEquals("No" + System.lineSeparator(),
                    FileUtils.readFileToString(new File("test/example9.syn"), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}