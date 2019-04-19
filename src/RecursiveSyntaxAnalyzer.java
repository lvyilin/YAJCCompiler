import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class RecursiveSyntaxAnalyzer extends SyntaxAnalyzer {
    private int lineNumber = 0;

    public RecursiveSyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter,
                                   Nonterminal startSymbol, HashMap<Nonterminal, ProductionRule> productionRules) {
        super(bufferedReader, bufferedWriter, startSymbol, productionRules);
    }

    private void A() throws SyntaxException {
        LexicalToken token;
        int oldLineNumber = getLineNumber();
        try {
            token = nextToken();
            if (token == null) throw new SyntaxException(null, getLineNumber());
            if (!token.matches("+") && !token.matches("-")) {
                revertToken();
                V();
                token = nextToken();
                if (token == null || !token.matches("=")) throw new SyntaxException(token, getLineNumber());
                E();
            }

        } catch (SyntaxException e) {
//            revertToken(oldLineNumber);
            throw e;
        }

    }

    private void V() throws SyntaxException {
        LexicalToken token = nextToken();
        if (token == null || !token.isIdentifier()) {
            revertToken();
            throw new SyntaxException(token, getLineNumber());
        }
    }

    private void F() throws SyntaxException {
        LexicalToken token;
        int oldLineNumber = getLineNumber();
        try {
            token = nextToken();
            if (token == null || !token.matches("(")) throw new SyntaxException(token, getLineNumber());
            E();
            token = nextToken();
            if (token == null || !token.matches(")")) throw new SyntaxException(token, getLineNumber());
        } catch (SyntaxException | NullPointerException e) {
            revertToken(oldLineNumber);
            token = nextToken();
            if (token == null || !token.isIdentifier()) {
                revertToken();
                throw e;
            }
        }

    }

    private void E() throws SyntaxException {
        T();
        E_();
    }

    private void T() throws SyntaxException {
        F();
        T_();
    }

    private void T_() throws SyntaxException {
        int oldLineNumber = getLineNumber();
        try {
            M();
        } catch (SyntaxException ignored) {
            revertToken(oldLineNumber);
            return;
        }
        F();
        T_();
    }

    private void E_() throws SyntaxException {
        int oldLineNumber = getLineNumber();
        try {
            A();
        } catch (SyntaxException ignored) {
            revertToken(oldLineNumber);
            return;
        }
        T();
        E_();
    }

    private void M() throws SyntaxException {
        LexicalToken token = nextToken();
        if (token == null || !token.matches("*") && !token.matches("/")) {
            revertToken();
            throw new SyntaxException(token, getLineNumber());
        }
    }


    private void Entrance() throws SyntaxException {
        if (!hasNextToken()) {
            return;
        }
        Stmt();
        Entrance();
    }

    private void Stmt() throws SyntaxException {
        int oldLineNumber = getLineNumber();
        try {
            If();
        } catch (SyntaxException ignored) {
            revertToken(oldLineNumber);
            A();
            LexicalToken token;
            token = nextToken();
            if (token == null || !token.matches(";")) throw new SyntaxException(token, getLineNumber());

        }
    }

    private void If() throws SyntaxException {
        LexicalToken token;
        token = nextToken();
        if (token == null || !token.matches("if")) throw new SyntaxException(token, getLineNumber());
        token = nextToken();
        if (token == null || !token.matches("(")) throw new SyntaxException(token, getLineNumber());
        Condition();
        token = nextToken();
        if (token == null || !token.matches(")")) throw new SyntaxException(token, getLineNumber());
        Clause();
    }


    private void Condition() throws SyntaxException {
        LexicalToken token = nextToken();
        if (token == null || !token.matches("1") && !token.matches("0")) {
            revertToken();
            throw new SyntaxException(token, getLineNumber());
        }
    }


    private void Clause() throws SyntaxException {
        LexicalToken token;
        int oldLineNumber = getLineNumber();
        try {
            token = nextToken();
            if (token == null || !token.matches("{")) throw new SyntaxException(token, getLineNumber());
            Entrance();
            token = nextToken();
            if (token == null || !token.matches("}")) throw new SyntaxException(token, getLineNumber());
        } catch (SyntaxException e) {
            revertToken(oldLineNumber);
            oldLineNumber = getLineNumber();
            try {
                Stmt();
            } catch (SyntaxException se) {
                revertToken(oldLineNumber);
                throw se;
            }
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public LexicalToken nextToken() {
        if (!hasNextToken()) return null;
        return getLexicalTokens().get(lineNumber++);
    }

    public boolean hasNextToken() {
        return lineNumber >= 0 & lineNumber < getLexicalTokens().size();
    }

    public void revertToken() {
        --lineNumber;
    }

    public void revertToken(int newLineNumber) {
        lineNumber = newLineNumber;
    }

    public void analyze() throws IOException {
        try {
            readAllLexicalTokens();
            Entrance();
            if (hasNextToken()) throw new SyntaxException(nextToken(), getLineNumber());
            writeResult("Yes" + System.lineSeparator());
        } catch (SyntaxException se) {
            se.printStackTrace();
            writeResult("No" + System.lineSeparator());
        }
    }


}
