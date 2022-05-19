package ir.ac.kntu.style;

import ir.ac.kntu.Main;

import java.io.*;

/**
 * SolutionTest
 */
public class SolutionTest {

    public boolean test(File input, File output){
            try (BufferedReader reader = new BufferedReader(new FileReader(output));
                 InputStream in = new FileInputStream(input);
                 PrintStream printStream = new PrintStream(output)){

                System.setIn(in);
                System.setOut(printStream);
                Main.main(new String[]{});
                StringBuilder builder = new StringBuilder();
                String saver;
                while ((saver=reader.readLine())!=null){
                    builder.append(saver).append(System.lineSeparator());
                }
                return false;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

    public void test1() {
        
    }

}