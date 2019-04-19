import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class SyntaxAnalyzer {
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private List<LexicalToken> lexicalTokens = new ArrayList<>();

    public HashMap<Nonterminal, ProductionRule> getProductionRules() {
        return productionRules;
    }

    public Nonterminal getStartSymbol() {
        return startSymbol;
    }

    private HashMap<Nonterminal, ProductionRule> productionRules;
    private Nonterminal startSymbol;

    public List<LexicalToken> getLexicalTokens() {
        return lexicalTokens;
    }


    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public SyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter,
                          Nonterminal startSymbol, HashMap<Nonterminal, ProductionRule> productionRules) {
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.productionRules = productionRules;
        this.startSymbol = startSymbol;
    }


    public abstract void analyze() throws IOException;


    public void writeResult(String s) throws IOException {
        bufferedWriter.write(s);
        bufferedWriter.flush();
    }

    public void readAllLexicalTokens() throws IOException {
        while (true) {
            String line = getBufferedReader().readLine();
            if (line == null) break;
            lexicalTokens.add(LexicalToken.parseToken(line));
        }
    }
}