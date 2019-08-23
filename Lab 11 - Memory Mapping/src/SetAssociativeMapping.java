import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

/**
 * JAVA program to demonstrate the Set Associative
 * memory mapping technique
 *
 * @author jimil
 */
public class SetAssociativeMapping {

    static ArrayList<CacheSet> cache = new ArrayList<CacheSet>();
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int hitCount = 0;
    static int faultCount = 0;
    static int w = 0;
    static int r = 0;
    static int tagSize = 0;
    static int sw = 0;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.print("Enter the size of main memory (in n-bit address): ");
        sw = Integer.parseInt(br.readLine());
        System.out.println();
        System.out.print("Enter the size of cache (in Kb): ");
        int cacheSize = Integer.parseInt(br.readLine());
        System.out.println();
        System.out.print("Enter the size of block (in bytes): ");
        int blockSize = Integer.parseInt(br.readLine());
        System.out.println();
        boolean flag = false;
        int linePerSet = 0;
        while(!flag) {
            System.out.print("Enter the no of lines in each set: ");
            linePerSet = Integer.parseInt(br.readLine());
            for(int i = 1; i <= 5; i++) {
                if(i == Math.log(linePerSet)/Math.log(2)) {
                    flag = true;
                    break;
                }
            }
            if(flag == false) {
                System.out.println("No. of lines per set should be in powers of 2. Please retry!");
            }
        }
        System.out.println();
        w = (int)(Math.log((double)blockSize)/Math.log(2));
        int x = (cacheSize*1024)/(blockSize*linePerSet);
        r = (int)(Math.log((double)x)/Math.log(2));
        tagSize = sw - r - w;
        System.out.println("Memory Config: ");
        System.out.println("Tag(" + tagSize + " bits), Set(" + r + " bits), Word(" + w + " bits)");
        for(int i = 0; i < x; i++) {
            CacheSet set = new CacheSet();
            set.setId(i);
            for(int j = 0; j < linePerSet; j++) {
                CacheLineDTO l = new CacheLineDTO();
                l.setSet(i);
                l.setLine(j);
                set.add(l);
            }
            cache.add(set);
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
        String set = addr.substring(tagSize, addr.length() - w);
        String word = addr.substring(addr.length() - w);
        System.out.println("["+tag+"],["+set+"],["+word+"]");
        int cacheSet = Integer.parseInt(set, 2);
        for(CacheSet cacheSetObj: cache) {
            if(cacheSetObj.getId() == cacheSet) {
                boolean flag = false;
                for(int i = 0; i < cacheSetObj.getLineSize(); i++) {
                    CacheLineDTO cacheLineDTO = cacheSetObj.get(i);
                    if(cacheLineDTO.getTag() == null) {
                        break;
                    }
                    if(cacheLineDTO.getTag().equalsIgnoreCase(tag)) {
                        System.out.println("Cache Hit!");
                        hitCount++;
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    faultCount++;
                    System.out.println("Cache Miss! Updating tag of line 0 to " + tag);
                    cacheSetObj.get(0).setTag(tag);
                    break;
                }
            }
        }
    }

}

/**
 * A POJO that models a cache line
 *
 * @author jimil
 */
class CacheLineDTO {
    private String tag;
    private int set;
    private int line;
    private int word;
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public int getSet() {
        return set;
    }
    public void setSet(int set) {
        this.set = set;
    }
    public int getLine() {
        return this.line;
    }
    public void setLine(int line) {
        this.line = line;
    }
    public int getWord() {
        return word;
    }
    public void setWord(int word) {
        this.word = word;
    }
    @Override
    public String toString() {
        return "{ Tag = "+tag+", Set = " + set + ", Line = " + line + " }";
    }
}

/**
 * POJO class that models a cache set -
 * a collection of lines
 *
 * @author jimil
 */
class CacheSet {

    private int id;
    private ArrayList<CacheLineDTO> lines = new ArrayList<>();

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void add(CacheLineDTO cacheLineDTO) {
        if(this.lines == null) {
            this.lines = new ArrayList<>();
        }
        this.lines.add(cacheLineDTO);
    }

    public CacheLineDTO get(int index) {
        if(index >= 0) {
            return this.lines.get(index);
        }
        return null;
    }

    public int getLineSize() {
        if(this.lines != null) {
            return this.lines.size();
        }
        return 0;
    }
}