public class SyntaxException extends Exception {
    public SyntaxException(LexicalToken lexicalToken, int lineNumber) {
        super(String.format("Line %d: (", lineNumber) + lexicalToken + ")" + System.lineSeparator());
    }
}
