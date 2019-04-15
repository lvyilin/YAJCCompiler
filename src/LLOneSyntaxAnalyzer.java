import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class LLOneSyntaxAnalyzer extends SyntaxAnalyzer {
    public LLOneSyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        super(bufferedReader, bufferedWriter);
    }

    @Override
    public void analyze() throws IOException {
        super.analyze();
        try {
            if (hasNextToken()) throw new SyntaxException(nextToken(), getLineNumber());
            writeResult("Yes" + System.lineSeparator());
        } catch (SyntaxException se) {
            se.printStackTrace();
            writeResult("No" + System.lineSeparator());
        }
    }
}
