import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SLROneSyntaxAnalyzerTest {
    private Compiler compiler = CompilerInstance.compiler5;

    @Test
    void testCase1() {
        String s = "test/example3.c";
        try {
            compiler.compile(s);
            assertEquals("*\ti\tk\t__TMP11__\r\n" +
                            "+\tj\t__TMP11__\t__TMP12__\r\n" +
                            "+\t__TMP12__\tl\t__TMP16__\r\n" +
                            "/\t__TMP16__\ti\t__TMP19__\r\n" +
                            "=\t__TMP19__\tnull\tabc\r\n",
                    FileUtils.readFileToString(new File("test/example3.syn"), Charset.defaultCharset()));
        } catch (IOException | IllegalSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCase2() {
        String s = "test/example4.c";
        try {
            compiler.compile(s);
            assertEquals("*\ti\tj\t__TMP10__\r\n" +
                            "-\tk\ti\t__TMP16__\r\n" +
                            "No\r\n",
                    FileUtils.readFileToString(new File("test/example4.syn"), Charset.defaultCharset()));
        } catch (IOException | IllegalSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCase3() {
        String s = "test/example10.c";
        try {
            compiler.compile(s);
            assertEquals("*\ti\tj\t__TMP10__\r\n" +
                            "+\t__TMP10__\tk\t__TMP15__\r\n" +
                            "-\t__TMP15__\ti\t__TMP16__\r\n" +
                            "+\t__TMP16__\tn\t__TMP20__\r\n" +
                            "=\t__TMP20__\tnull\tabc\r\n",
                    FileUtils.readFileToString(new File("test/example10.syn"), Charset.defaultCharset()));
        } catch (IOException | IllegalSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCase4() {
        String s = "test/example11.c";
        try {
            compiler.compile(s);
            assertEquals("*\tC\tD\t__TMP11__\r\n" +
                            "+\tB\t__TMP11__\t__TMP12__\r\n" +
                            "=\t__TMP12__\tnull\tX\r\n",
                    FileUtils.readFileToString(new File("test/example11.syn"), Charset.defaultCharset()));
        } catch (IOException | IllegalSyntaxException e) {
            e.printStackTrace();
        }
    }
}