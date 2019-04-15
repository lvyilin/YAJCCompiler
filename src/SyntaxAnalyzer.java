import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SyntaxAnalyzer {

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int lineNumber = 0;
    private List<LexicalToken> tokens = new ArrayList<>();

    public SyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
    }

    public int getLineNumber() {
        return lineNumber;
    }


    public LexicalToken nextToken() {
        if (!hasNextToken()) return null;
        return tokens.get(lineNumber++);
    }

    public boolean hasNextToken() {
        return lineNumber >= 0 & lineNumber < tokens.size();
    }

    public void revertToken() {
        --lineNumber;
    }

    public void revertToken(int newLineNumber) {
        lineNumber = newLineNumber;
    }

    public void analyze() throws IOException {
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) break;
            tokens.add(LexicalToken.parseToken(line));
        }
    }

    public void writeResult(String s) throws IOException {
        bufferedWriter.write(s);
//        bufferedWriter.flush();
    }
}