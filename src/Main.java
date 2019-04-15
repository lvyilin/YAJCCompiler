import java.io.IOException;

public class Main {


    public static void main(String[] args) {
//        String testFileName1 = "test/example1.c";
//        String testFileName2 = "test/example2.c";
//        String testFileName3 = "test/example3.c";
//        String testFileName3 = "test/example4.c";
        String testFileName5 = "test/example5.c";
        String testFileName6 = "test/example6.c";
        try {
//            Compiler.compile(testFileName1);
//            Compiler.compile(testFileName2);
//            Compiler.compile(testFileName3);
//            Compiler.compile(testFileName4);
            Compiler.compile(testFileName5);
            Compiler.compile(testFileName6);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
