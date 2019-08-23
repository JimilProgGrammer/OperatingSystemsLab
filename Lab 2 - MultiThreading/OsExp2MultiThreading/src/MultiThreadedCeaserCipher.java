import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A Java program to demonstrate the Caesar cipher encryption technique
 * using multiple threads
 *
 * @author jimil
 */
public class MultiThreadedCeaserCipher {

    /**
     * Tons of class variables
     */
    private BufferedReader fileReader;
    private static ArrayList<String> input = new ArrayList<>();
    private static ArrayList<Thread> threads = new ArrayList<>();
    private static List<List<String>> chunks = new ArrayList<>();
    public static List<List<String>> cipher = new ArrayList<>();
    public static List<List<String>> plaintext = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        MultiThreadedCeaserCipher mulCipher = new MultiThreadedCeaserCipher();
        mulCipher.loadInput();
        chunks = Lists.partition(input, 75);
        int numberOfThreads = chunks.size();
        System.out.println("Creating " + numberOfThreads + " threads.");
        mulCipher.initThreads(numberOfThreads);
        long startTime = System.currentTimeMillis();
        for(Thread thread: threads) {
            thread.start();
        }
        for(Thread thread: threads) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        //printCipher();
        mulCipher.initDecryptThreads(numberOfThreads);
        long startTime1 = System.currentTimeMillis();
        for(Thread thread: threads) {
            thread.start();
        }
        for(Thread thread: threads) {
            thread.join();
        }
        long endTime1 = System.currentTimeMillis();
        //printPlainText();
        System.out.println("Time Required(in ms) = " + (endTime-startTime + endTime1-startTime1));
    }

    /**
     * This function takes the number of threads to be created as input
     * and creates that many threads in a pool (represented by an ArrayList);
     * at the same time, this function also injects the chunk of text that
     * the thread is supposed to encrypt
     *
     * @param n
     */
    public void initThreads(int n) {
        System.out.println("IN initThreads(): Initializing thread pool...");
        for(int i = 0; i < n; i++) {
            EncryptionThread newThread = new EncryptionThread("Encryption Thread " + i);
            newThread.setData(chunks.get(i));
            threads.add(newThread);
        }
        System.out.println("IN initThreads(): Threads initialized...");
    }

    /**
     * This function takes the number of threads to be created as input
     * and creates that many threads in a pool (represented by an ArrayList);
     * at the same time, this function also injects the chunk of text that
     * the thread is supposed to decrypt
     *
     * @param n
     */
    public void initDecryptThreads(int n) {
        threads.clear();
        System.out.println("IN initThreads(): Initializing decryption thread pool...");
        for(int i = 0; i < n; i++) {
            DecryptionThread newThread = new DecryptionThread("Decryption Thread " + i);
            newThread.setData(cipher.get(i));
            threads.add(newThread);
        }
        System.out.println("IN initThreads(): Decryption Threads initialized...");
    }

    /**
     * This function loads data from a predefined file path into a list
     * of strings
     *
     * @throws Exception
     */
    public void loadInput() throws Exception {
        String home = System.getProperty("user.home");
        File ipFile = new File(home + "/Documents/plaintext.text");
        this.fileReader = new BufferedReader(new FileReader(ipFile));
        System.out.println("IN loadInput(): FileReader initialized successfully...");
        String line;
        while((line = fileReader.readLine()) != null) {
            this.input.add(line);
        }
        System.out.println("IN loadInput(): Input data loaded successfully...");
    }

    /**
     * Simple function to print the cipher
     */
    public static void printCipher() {
        if(!cipher.isEmpty()) {
            for(List<String> list: cipher) {
                for(String str: list) {
                    System.out.println(str);
                }
            }
        }
    }

    /**
     * Utility function to print decrypted plaintext
     */
    public static void printPlainText() {
        if(!plaintext.isEmpty()) {
            for(List<String> list: plaintext) {
                for(String str: list) {
                    System.out.println(str);
                }
            }
        }
    }

}

/**
 * A class that represents a thread that is equipped with the task
 * of encrypting the chunk of text that it is passed.
 */
class EncryptionThread extends Thread {
    private List<String> data = new ArrayList<>();
    private List<String> cipher = new ArrayList<>();

    /**
     * Constructor that is used to set the name of this
     * thread.
     *
     * @param name
     */
    EncryptionThread(String name) {
        super(name);
    }

    /**
     * Used for dependency injection i.e. to pass the chunk that this
     * thread has to encrypt
     *
     * @param data
     */
    public void setData(List<String> data) {
        this.data = data;
    }

    /**
     * Function that encrypts the line that is passed to it
     *
     * @param line
     */
    private void encrypt(String line) {
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
        this.cipher.add(sb.toString());
    }

    /**
     * Implementation definition for base class
     * method.
     */
    @Override
    public void run() {
        if(!this.data.isEmpty()) {
            //System.out.println(Thread.currentThread().getName() + "; IN run(): encrypting...");
            for(String str: this.data) {
                encrypt(str);
            }
            //System.out.println(Thread.currentThread().getName() + "; IN run(): Done encrypting...");
            MultiThreadedCeaserCipher.cipher.add(this.cipher);
        } else {
            System.out.println(Thread.currentThread().getName() + "IN run(): Input data is empty");
        }
    }

}

/**
 * A class that represents a thread that is equipped with the task of
 * decrypting a chunk of ciphertext passed to it.
 */
class DecryptionThread extends Thread {
    private List<String> data = new ArrayList<>();
    private List<String> deciphered = new ArrayList<>();

    /**
     * Constructor to configure the name for this decryption
     * thread instance
     *
     * @param name
     */
    DecryptionThread(String name) {
        super(name);
    }

    /**
     * Used for dependency injection i.e. to pass the chunk that this
     * thread has to decrypt
     *
     * @param data
     */
    public void setData(List<String> data) {
        this.data = data;
    }

    /**
     * Function that decrypts the line that is passed to it
     *
     * @param line
     */
    private void decrypt(String line) {
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
        this.deciphered.add(sb.toString());
    }

    /**
     * Implementation definition for base class
     * method.
     */
    @Override
    public void run() {
        if(!this.data.isEmpty()) {
            //System.out.println(Thread.currentThread().getName() + "; IN run(): decrypting...");
            for(String str: this.data) {
                decrypt(str);
            }
            //System.out.println(Thread.currentThread().getName() + "; IN run(): Done decrypting...");
            MultiThreadedCeaserCipher.plaintext.add(this.deciphered);
        } else {
            System.out.println(Thread.currentThread().getName() + "IN run(): Input cipher is empty");
        }
    }
}