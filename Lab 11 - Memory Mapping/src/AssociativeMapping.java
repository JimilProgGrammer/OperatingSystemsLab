import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

/**
 * JAVA class to demonstrate the fully associative
 * memory mapping technique.
 *
 * @author jimil
 */
public class AssociativeMapping {

    static ArrayList<CacheLine> cache = new ArrayList<CacheLine>();
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int hitCount = 0;
    static int faultCount = 0;
    static int w = 0;
    static int tagSize = 0;
    static int sw = 0;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.print("Enter the size of main memory (in n-bit address): ");
        sw = Integer.parseInt(br.readLine());
        System.out.println();
        System.out.print("Enter the size of block (in bytes): ");
        int blockSize = Integer.parseInt(br.readLine());
        System.out.println();
        w = (int)(Math.log((double)blockSize)/Math.log(2));
        tagSize = sw - w;
        System.out.println("Memory Config: ");
        System.out.println("Tag(" + tagSize + " bits), Word(" + w + " bits)");
        for(int i = 0; i < 100; i++) {
            CacheLine cl = new CacheLine();
            cache.add(cl);
        }
        int ch = 1;
        int count = 0;
        do {
            count++;
            System.out.println("Enter a physical address to check: ");
            int address = Integer.parseInt(br.readLine());
            String addr = Integer.toBinaryString(address);
            if(addr.length() != sw) {
                addr = StringUtils.leftPad(addr, sw, "0");
                System.out.println("Checking for address: " + addr);
            }
            check(addr);
            System.out.println("Press 1. to check another address and 0. to exit");
            ch = Integer.parseInt(br.readLine());
        } while(ch != 0);
        System.out.println("Hit Ratio = " + (double)hitCount/(double)count);
        System.out.println("Fault Ratio = " + (double)faultCount/(double)count);
    }

    /**
     * A utility method that checks for the physical address
     * sent to it, inside cache and determines whether it is
     * a page fault or a page hit.
     *
     * @param addr: physical address to check for, in cache.
     */
    public static void check(String addr) {
        String tag = addr.substring(0, tagSize);
        String word = addr.substring(addr.length() - w);
        System.out.println("["+tag+"],["+word+"]");
        for(CacheLine lineObj: cache) {
            if(tag.equalsIgnoreCase(lineObj.getTag())) {
                hitCount++;
                System.out.println("Cache Hit!");
                break;
            } else {
                faultCount++;
                System.out.println("Cache Miss!");
                System.out.println("Updating tag to " + tag);
                lineObj.setTag(tag);
                break;
            }
        }
    }

}
/**
 * A POJO that models a cache line
 *
 * @author jimil
 */
class CacheLine {

    private String tag;
    private int word;
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public int getWord() {
        return word;
    }
    public void setWord(int word) {
        this.word = word;
    }

}