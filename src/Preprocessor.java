import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Preprocessor {
    public static void process(BufferedReader br, BufferedWriter bw) throws IOException {
        while (true) {
            int ch = br.read();
            if (ch == -1) break;
            char c = (char) ch;
            if (c == '/') {
                br.mark(1);
                char nextC = (char) br.read();
                if (nextC == '/') {
                    br.readLine();
                } else if (nextC == '*') {
                    // read until "*/"
                    int endC = br.read();
                    while (endC != -1) {
                        while (endC != -1 && (char) endC != '*') {
                            endC = br.read();
                        }
                        endC = br.read();
                        if ((char) endC == '/') {
                            break;
                        }
                    }

                } else {
                    br.reset();
                    bw.write(c);
                }
            } else if (c == ' ') {
                bw.write(c);
                while (ch != -1 && (char) ch == ' ') {
                    br.mark(1);
                    ch = br.read();
                }
                br.reset();

            } else if (!Character.isWhitespace(c)) {
                bw.write(c);
            }
        }
        bw.flush();
    }
}
