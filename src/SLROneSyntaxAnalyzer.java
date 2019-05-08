import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

public class SLROneSyntaxAnalyzer extends SyntaxAnalyzer {
    public SLROneSyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter, Nonterminal startSymbol, HashMap<Nonterminal, ProductionRule> productionRules) {
        super(bufferedReader, bufferedWriter, startSymbol, productionRules);
    }

    @Override
    public void analyze() throws IOException {

    }
}
