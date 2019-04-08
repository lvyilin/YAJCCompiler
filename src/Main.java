import java.io.IOException;

public class Main {


    public static void main(String[] args) {
        String testFileName1 = "example1.c";
        String testFileName2 = "example2.c";
        try {
            Compiler.compile(testFileName1);
            Compiler.compile(testFileName2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
