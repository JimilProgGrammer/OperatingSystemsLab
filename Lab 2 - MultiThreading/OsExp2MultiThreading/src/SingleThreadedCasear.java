import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * A Java class to demonstrate the Caesar Cipher encryption technique
 * using a single thread.
 *
 * @author jimil
 */
public class SingleThreadedCasear {

    public static void main(String[] args) throws Exception {
        /**
         * Configuring the input path
         */
        String home = System.getProperty("user.home");
        File ipFile = new File(home + "/Documents/plaintext.text");
        BufferedReader fileReader = new BufferedReader(new FileReader(ipFile));
        String line;
        ArrayList<String> input = new ArrayList<>();
        ArrayList<String> cipher = new ArrayList<>();
        ArrayList<String> deciphered = new ArrayList<>();
        /**
         * Reading the input from the file
         */
        while((line = fileReader.readLine()) != null) {
            input.add(line);
        }
        long startTime = System.currentTimeMillis();
        /**
         * Encrypting all lines read from input
         */
        for(String str: input) {
            cipher.add(encrypt(str));
        }
        /**
         * Decrypting all the Strings in cipher
         */
        for(String str: cipher) {
            deciphered.add(decrypt(str));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time Required(in ms) = " + (endTime-startTime));
    }

    /**
     * Encrypts the input line using Caesar cipher with
     * size of cipher = 3
     *
     * @param line
     * @return
     */
    public static String encrypt(String line) {
        int len = line.length();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < len; i++) {
            char ch = line.charAt(i);
            if((int)ch == 32)
                sb.append(" ");
            else if(ch != 'X' && ch != 'x' && ch != 'Y' && ch != 'y' && ch != 'Z' && ch != 'z')
                sb.append((char)((int)ch+3));
            else if(ch == 'X')
                sb.append('A');
            else if(ch == 'x')
                sb.append('a');
            else if(ch == 'Y')
                sb.append('B');
            else if(ch == 'y')
                sb.append('b');
            else if(ch == 'Z')
                sb.append('C');
            else if(ch == 'z')
                sb.append('c');
            else if(ch == ' ')
                sb.append(' ');
        }
        return sb.toString();
    }

    /**
     * Decrypts the cipher with secret key = 3
     *
     * @param line
     * @return
     */
    public static String decrypt(String line) {
        int len = line.length();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < len; i++) {
            char ch = line.charAt(i);
            if((int)ch == 32)
                sb.append(" ");
            else if(ch == 'A')
                sb.append('X');
            else if(ch == 'a')
                sb.append('x');
            else if(ch == 'B')
                sb.append('Y');
            else if(ch == 'b')
                sb.append('y');
            else if(ch == 'C')
                sb.append('Z');
            else if(ch == 'c')
                sb.append('z');
            else
                sb.append((char)((int)ch-3));
        }
        return sb.toString();
    }

}
