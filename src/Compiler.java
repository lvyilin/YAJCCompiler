import java.io.*;

public class Compiler {
    public static void compile(String fileName) throws IOException {
        String baseName = getBaseName(fileName);
        String preprocessedFileName = baseName + ".i";
        String lexicalFileName = baseName + ".lex";
        String syntaxFileName = baseName + ".syn";


        BufferedReader br1 = new BufferedReader(new FileReader(fileName));
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(preprocessedFileName));
        Preprocessor.process(br1, bw1);
        br1.close();
        bw1.close();

        BufferedReader br2 = new BufferedReader(new FileReader(preprocessedFileName));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(lexicalFileName));
        LexicalAnalyzer.analyze(br2, bw2);
        br2.close();
        bw2.close();

        BufferedReader br3 = new BufferedReader(new FileReader(lexicalFileName));
        BufferedWriter bw3 = new BufferedWriter(new OutputStreamWriter(System.out));
//        BufferedWriter bw3 = new BufferedWriter(new FileWriter(syntaxFileName));

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(br3, bw3);
        syntaxAnalyzer.analyze();
        br3.close();
        bw3.flush();

    }

    public static String getBaseName(String fileName) {
        if (fileName == null) return null;
        int i = fileName.lastIndexOf('.');
        if (i != -1) {
            return fileName.substring(0, i);
        }
        return fileName;
    }
}
