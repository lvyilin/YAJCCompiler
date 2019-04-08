public class LexicalToken {
    private int tokenId;
    private String tokenString;
    public static final int MAX_TOKEN_LENGTH = 64;

    public LexicalToken(int tokenId, String tokenString) {
        this.tokenId = tokenId;
        this.tokenString = tokenString;
    }

    @Override
    public String toString() {
        return tokenId + "\t" + tokenString + System.lineSeparator();
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public int getTokenId() {
        return tokenId;
    }

    public String getTokenString() {
        return tokenString;
    }

    public static LexicalToken parseToken(String line) {
        String[] content = line.split("\t");
        return new LexicalToken(Integer.parseInt(content[0]), content[1]);
    }

    public boolean matches(String s) {
        return tokenString.equals(s);
    }

    public boolean isIdentifier() {
        return getTokenId() == GrammarConsts.ID;
    }
}
