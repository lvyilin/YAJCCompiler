import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SyntaxAnalyzer {
    private BufferedReader br;
    private BufferedWriter bw;
    private int lineNumber = 0;

    public SyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        this.br = bufferedReader;
        this.bw = bufferedWriter;
    }

    private void A_() throws IOException, SyntaxException {
        LexicalToken token = null;
        try {
            V_();
            token = nextToken();
            if (token == null || !token.matches("=")) throw new SyntaxException(Integer.toString(lineNumber));
            E_();
        } catch (SyntaxException | NullPointerException e) {
            revertToken();
            token = nextToken();
            if (token == null || (!token.matches("+") && !token.matches("-"))) throw e;
        }

    }

    private void V_() throws IOException, SyntaxException {
        LexicalToken token = nextToken();
        if (token == null || !token.isIdentifier()) throw new SyntaxException(Integer.toString(lineNumber));
    }

    private void F_() throws IOException, SyntaxException {
        LexicalToken token = null;
        try {
            token = nextToken();
            if (token == null || !token.matches("(")) throw new SyntaxException(Integer.toString(lineNumber));
            E_();
            token = nextToken();
            if (token == null || !token.matches(")")) throw new SyntaxException(Integer.toString(lineNumber));
        } catch (SyntaxException | NullPointerException e) {
            if (token == null || !token.isIdentifier()) throw e;
        }

    }

    private void E_() throws IOException, SyntaxException {
        T_();
        E__();
    }

    private void T_() throws IOException, SyntaxException {
        F_();
        T__();
    }

    private void T__() throws IOException, SyntaxException {
        try {
            M_();
            F_();
            T__();
        } catch (SyntaxException ignored) {
            revertToken();
        }
    }

    private void E__() throws IOException, SyntaxException {
        try {
            A_();
            T_();
            E_();
        } catch (SyntaxException ignored) {
            revertToken();
        }
    }

    private void M_() throws IOException, SyntaxException {
        LexicalToken token = nextToken();
        if (token == null || !token.matches("*") && !token.matches("/"))
            throw new SyntaxException(Integer.toString(lineNumber));
    }


    public void analyze() throws IOException {
        try {
            A_();
            LexicalToken token = nextToken();
            if (token != null) throw new SyntaxException(Integer.toString(lineNumber));
            bw.write("Yes");
        } catch (SyntaxException se) {
            se.printStackTrace();
            bw.write("No");
        }
        bw.flush();
    }

    private LexicalToken nextToken() throws IOException {
        br.mark(LexicalToken.MAX_TOKEN_LENGTH);
        String line = br.readLine();
        if (line == null) return null;
        ++lineNumber;
        return LexicalToken.parseToken(line);
    }

    private boolean hasNextToken() throws IOException {
        br.mark(1);
        int ch = br.read();
        if (ch != -1) {
            br.reset();
            return true;
        }
        return false;
    }

    private void revertToken() throws IOException {
        br.reset();
        --lineNumber;
    }
}
