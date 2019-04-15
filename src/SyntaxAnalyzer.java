import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public abstract class SyntaxAnalyzer {

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public SyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
    }

    public abstract int getLineNumber();

    public abstract LexicalToken nextToken();

    public abstract boolean hasNextToken();

    public abstract void revertToken();

    public abstract void revertToken(int newLineNumber);

    public abstract void analyze() throws IOException;


    public void writeResult(String s) throws IOException {
        bufferedWriter.write(s);
        bufferedWriter.flush();
    }
}