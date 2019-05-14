import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RecursiveSyntaxAnalyzerTest {
    private Compiler compiler = new Compiler();

    @BeforeEach
    void beforeEach() {
        compiler.setSyntaxAnalyzerType(Compiler.SyntaxAnalyzeEnum.RECURSIVE);
    }

    @Test
    void testCase1() {
        String s = "test/example5.c";
        try {
            compiler.compile(s);
            assertEquals("Yes" + System.lineSeparator(),
                    FileUtils.readFileToString(new File("test/example5.syn"), Charset.defaultCharset()));
        } catch (IOException | IllegalSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCase2() {
        String s = "test/example6.c";
        try {
            compiler.compile(s);
            assertEquals("No" + System.lineSeparator(),
                    FileUtils.readFileToString(new File("test/example6.syn"), Charset.defaultCharset()));
        } catch (IOException | IllegalSyntaxException e) {
            e.printStackTrace();
        }
    }
}