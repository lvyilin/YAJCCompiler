import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class LLOneSyntaxAnalyzer extends SyntaxAnalyzer {
    public LLOneSyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        super(bufferedReader, bufferedWriter);
    }

    @Override
    public int getLineNumber() {
        return 0;
    }

    @Override
    public LexicalToken nextToken() {
        return null;
    }

    @Override
    public boolean hasNextToken() {
        return false;
    }

    @Override
    public void revertToken() {

    }

    @Override
    public void revertToken(int newLineNumber) {

    }

    @Override
    public void analyze() throws IOException {
        try {
            if (hasNextToken()) throw new SyntaxException(nextToken(), getLineNumber());
            writeResult("Yes" + System.lineSeparator());
        } catch (SyntaxException se) {
            se.printStackTrace();
            writeResult("No" + System.lineSeparator());
        }
    }
}
