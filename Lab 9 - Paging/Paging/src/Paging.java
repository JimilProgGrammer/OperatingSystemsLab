import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A Java program to demonstrate the various
 * page replacement algorithms
 *
 * @author jimil
 */
public class Paging {

    public static final int MAX_FRAME_SIZE = 3;
    public static int tick;
    public static List<Page> mainMemory = new ArrayList<Page>();
    public static List<Page> availablePages = new ArrayList<Page>();
    public static List<Integer> pageStream = new ArrayList<Integer>();
    public static int streamSize;
    public static int numberOfPages;
    public static int hitCount;
    public static int faultCount;

    /**
     * Driver function for program
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        tick = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the size of the page stream: ");
        streamSize = Integer.parseInt(br.readLine());
        System.out.println("Enter the number of pages: ");
        numberOfPages = Integer.parseInt(br.readLine());

        for(int i = 0; i < numberOfPages; i++) {
            Page page = new Page();
            page.setId(i);
            availablePages.add(page);
        }

        System.out.println("Enter the page stream: ");
        for(int i = 0; i < streamSize; i++) {
            pageStream.add(Integer.parseInt(br.readLine()));
        }

        System.out.println("Select the page replacement policy to be implemented: ");
        System.out.println("1.	FIFO");
        System.out.println("2.	LRU");
        System.out.println("3.	Optimal Page Replacement");
        int choice = Integer.parseInt(br.readLine());
        switch(choice) {
            case 1: fifo();
                break;
            case 2: lru();
                break;
            case 3: optimalPageReplacement();
                break;
            default: System.out.println("Enter a valid choice!");
                break;
        }
    }

    /**
     * Function to demonstrate the action of
     * processor demanding a page from the cache.
     *
     * Can also be utilized to search for page
     * in main memory.
     *
     * @param pageNo
     * @return
     */
    public static boolean demand(int pageNo) {
        for(int i = 0; i < MAX_FRAME_SIZE; i++) {
            if(!mainMemory.isEmpty() && i < mainMemory.size()) {
                Page p = mainMemory.get(i);
                if(p != null && p.getId() == pageNo) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method that implements page replacement using fifo.
     */
    public static void fifo() {
        for(int pageNo: pageStream) {
            tick++;
            boolean result = demand(pageNo);
            if(result) {
                hitCount++;
                System.out.println("HIT! Page " + pageNo + " residing in cache!");
            } else {
                if(mainMemory.size() < MAX_FRAME_SIZE) {
                    faultCount++;
                    availablePages.get(pageNo).setStartTick(tick);
                    System.out.println("Main Memory is empty! Adding page: " + pageNo);
                    mainMemory.add(availablePages.get(pageNo));
                } else {
                    faultCount++;
                    int toReplacePage = findFirstIn();
                    System.out.println("PAGE FAULT! Replacing page: " + mainMemory.get(toReplacePage).getId());
                    mainMemory.remove(toReplacePage);
                    availablePages.get(pageNo).setStartTick(tick);
                    mainMemory.add(toReplacePage, availablePages.get(pageNo));
                }
            }
            System.out.println("Current Main Memory Status: ");
            for(Page p: mainMemory) {
                System.out.print(p.getId() + ", ");
            }
            System.out.println();
        }
        System.out.println("OUTPUT STATS: ");
        System.out.println("Hit Ratio = " + ((float)hitCount/(float)streamSize));
        System.out.println("Fault Count = " + faultCount);
        System.out.println("Hit Count = " + hitCount);
    }

    /**
     * Method that implements page replacement using the
     * LRU algorithm
     */
    public static void lru() {
        int currentIndex = -1;
        for(int pageNo: pageStream) {
            currentIndex++;
            boolean result = demand(pageNo);
            if(result) {
                hitCount++;
                System.out.println("HIT! Page " + pageNo + " residing in cache!");
            } else {
                if(mainMemory.size() < MAX_FRAME_SIZE) {
                    faultCount++;
                    availablePages.get(pageNo).setStartTick(tick);
                    System.out.println("Main Memory is empty! Adding page: " + pageNo);
                    mainMemory.add(availablePages.get(pageNo));
                } else {
                    faultCount++;
                    int toReplacePage = findLeastRecentlyUsed(currentIndex);
                    if(toReplacePage >= 0) {
                        System.out.println("PAGE FAULT! Replacing page: " + mainMemory.get(toReplacePage).getId());
                        mainMemory.remove(toReplacePage);
                        availablePages.get(pageNo).setStartTick(tick);
                        mainMemory.add(toReplacePage, availablePages.get(pageNo));
                    }
                }
            }
            System.out.println("Current Main Memory Status: ");
            for(Page p: mainMemory) {
                System.out.print(p.getId() + ", ");
            }
            System.out.println();
        }
        System.out.println("OUTPUT STATS: ");
        System.out.println("Hit Ratio = " + ((float)hitCount/(float)streamSize));
        System.out.println("Fault Count = " + faultCount);
        System.out.println("Hit Count = " + hitCount);
    }

    /**
     * Method that implements paging and uses optimal
     * page replacement strategy to handle page faults.
     */
    public static void optimalPageReplacement() {
        int currentIndex = -1;
        for(int pageNo: pageStream) {
            currentIndex++;
            boolean result = demand(pageNo);
            if(result) {
                hitCount++;
                System.out.println("HIT! Page " + pageNo + " residing in cache!");
            } else {
                if(mainMemory.size() < MAX_FRAME_SIZE) {
                    faultCount++;
                    System.out.println("Main Memory is empty! Adding page: " + pageNo);
                    mainMemory.add(availablePages.get(pageNo));
                } else {
                    faultCount++;
                    int toReplacePage = toReplaceUsingOptSolution(currentIndex);
                    if(toReplacePage >= 0) {
                        System.out.println("PAGE FAULT! Replacing page: " + mainMemory.get(toReplacePage).getId());
                        mainMemory.remove(toReplacePage);
                        mainMemory.add(toReplacePage, availablePages.get(pageNo));
                    }
                }
            }
            System.out.println("Current Main Memory Status: ");
            for(Page p: mainMemory) {
                System.out.print(p.getId() + ", ");
            }
            System.out.println();
        }
        System.out.println("OUTPUT STATS: ");
        System.out.println("Hit Ratio = " + ((float)hitCount/(float)streamSize));
        System.out.println("Fault Count = " + faultCount);
        System.out.println("Hit Count = " + hitCount);
    }

    /**
     * Returns the index of the page in main memory
     * that arrived first.
     *
     * @return
     */
    public static int findFirstIn() {
        int index = 0;
        int min = mainMemory.get(index).getStartTick();
        for(int i = 1; i < MAX_FRAME_SIZE; i++) {
            if(mainMemory.get(i).getStartTick() < min) {
                index = i;
                min = mainMemory.get(i).getStartTick();
            }
        }
        return index;
    }

    /**
     * Returns the index of the page in main memory
     * that is least recently used.
     *
     * Backtracks the page stream upto MAX_FRAME_SIZE
     * steps and returns the index of that page in main
     * memory.
     *
     * @param index
     * @return
     */
    public static int findLeastRecentlyUsed(int index) {
        List<Integer> stack = new ArrayList<Integer>();
        int count = 0;
        for(int i = index-1; i >= 0; i--) {
            if(!stack.contains(pageStream.get(i))) {
                stack.add(pageStream.get(i));
                count++;
            }
            if(count == MAX_FRAME_SIZE) {
                break;
            }
        }
        int pageId = (int)stack.get(stack.size() - 1);
        int returnIndex = -10;
        for(Page p: mainMemory) {
            if(p.getId() == pageId) {
                returnIndex = mainMemory.indexOf(p);
                break;
            }
        }
        return returnIndex;
    }

    /**
     * Returns the index of the page that is not likely to
     * be demanded in the near future.
     *
     * This is done by traversing the input page stream
     * from current index upto MAX_FRAME_SIZE steps and
     * comparing with available pages in memory to find out
     * the ones that are not going to be demanded in
     * the near future.
     *
     * @param index
     * @return
     */
    public static int toReplaceUsingOptSolution(int index) {
        List<Integer> list = new ArrayList<>();
        List<Integer> pageIds = new ArrayList<>();
        int count = 0;
        for(int i = index+1; i < pageStream.size(); i++) {
            if(!list.contains(pageStream.get(i))) {
                int pageId = pageStream.get(i);
                for(Page p: mainMemory) {
                    if(!pageIds.contains(p.getId())) {
                        pageIds.add(p.getId());
                    }
                    if (p.getId() == pageId) {
                        list.add(p.getId());
                        count++;
                        break;
                    }
                }
                if(count == MAX_FRAME_SIZE) {
                    break;
                }
            }
        }
        pageIds.removeAll(list);
        int returnIndex = -10;
        if(pageIds.size() >= 1) {
            int pageId = pageIds.get(pageIds.size() - 1);
            for(Page p: mainMemory) {
                if(p.getId() == pageId) {
                    returnIndex = mainMemory.indexOf(p);
                    break;
                }
            }
        } else {
            returnIndex = MAX_FRAME_SIZE-1;
        }
        return returnIndex;
    }
}

/**
 * A POJO that denotes a simple page in the
 * system.
 *
 * @author jimil
 */
class Page {
    private int id;
    private int useCount;
    private int startTick;
    private int frequency;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUseCount() {
        return useCount;
    }
    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }
    public int getStartTick() {
        return startTick;
    }
    public void setStartTick(int startTick) {
        this.startTick = startTick;
    }
    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}