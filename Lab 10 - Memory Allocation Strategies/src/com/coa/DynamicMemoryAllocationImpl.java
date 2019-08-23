package com.coa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * JAVA class to demonstrate the dynamic memory
 * allocation techniques.
 *
 * @author jimil
 */
public class DynamicMemoryAllocationImpl {
    static ArrayList<Process> processes = new ArrayList<Process>();
    static ArrayList<MemoryBlock> blocks = new ArrayList<MemoryBlock>();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        /**
         * Take process details as input from
         * the user.
         */
        System.out.print("Enter the number of processes: ");
        int processCount = Integer.parseInt(br.readLine());
        System.out.println();
        System.out.println("Enter the process details: ");
        for(int i = 0; i < processCount; i++) {
            Process p = new Process();
            p.setId(i);
            System.out.print("Enter the process' memory request size: ");
            p.setMemoryRequestSize(Integer.parseInt(br.readLine()));
            processes.add(p);
            System.out.println();
        }
        /**
         * Take details of the memory blocks as input
         * from the user.
         */
        System.out.print("Enter the number of memory blocks: ");
        int blocksCount = Integer.parseInt(br.readLine());
        System.out.println();
        System.out.println("Enter the memory block details: ");
        for(int i = 0; i < blocksCount; i++) {
            MemoryBlock mb = new MemoryBlock();
            mb.setId(i);
            System.out.print("Enter the size of the memory block: ");
            mb.setSize(Integer.parseInt(br.readLine()));
            mb.setAllocatedProcessId(-10);
            blocks.add(mb);
            System.out.println();
        }
        /**
         * Take choice of algorithm as input from the user.
         */
        System.out.println("1.		First Fit");
        System.out.println("2.		Best Fit");
        System.out.println("3.		Worst Fit");
        System.out.println("Which algorithm do you want to implement?");
        int choice = Integer.parseInt(br.readLine());
        switch(choice) {
            case 1: doFirstFit();
                    printDetails();
                    release(choice);
                break;
            case 2: doBestFit();
                    printDetails();
                    release(choice);
                break;
            case 3: doWorstFit();
                    printDetails();
                    release(choice);
                break;
            default: System.out.println("Please Enter a valid choice!");
                break;
        }
    }

    /**
     * Default implementation to perform dynamic memory
     * allocation using first fit algorithm.
     * The algorithm iterates over the list of processes
     * and does the following steps:
     *   1. For each process, iterate over the memory blocks
     *   	from the beginning
     *   2. The first block that is found to be free and of size
     *   	greater than the process' requested size, do the following:
     *   	2.1 Update the block's allocation status to true
     *   	2.2 Update the process' PCB to indicate the block it got allocated
     *   	2.3 Update the memory block's descriptor to indicate the process that is
     *   		currently residing in it.
     *   	2.4 New size of memory block =
     *   			size of memory block - process' requested memory size
     */
    public static void doFirstFit() {
        for(Process p: processes) {
            if(p.isAllocated() == false) {
                System.out.println("Searching for memory of size "
                        + p.getMemoryRequestSize() + " for process P" + p.getId());
                boolean flag = false;
                for(MemoryBlock mb: blocks) {
                    if(mb.getSize() >= p.getMemoryRequestSize() && !mb.isAllocated()) {
                        mb.setAllocatedProcessId(p.getId());
                        p.setMemoryBlockIdAllocated(mb.getId());
                        p.setAllocated(true);
                        mb.setAllocated(true);
                        MemoryBlock newMb = new MemoryBlock();
                        newMb.setSize(mb.getSize() - p.getMemoryRequestSize());
                        newMb.setId(1000 + mb.getId());
                        newMb.setAllocated(false);
                        newMb.setAllocatedProcessId(-10);
                        blocks.add(newMb);
                        System.out.println("Process P" + p.getId() + " allocated to memory block B"
                                + mb.getId());
                        System.out.println("New Partition created: B" + mb.getId()
                                + " of size: " + newMb.getSize());
                        flag = true;
                        break;
                    }
                }
                if(flag == false) {
                    System.out.println("Process P" + p.getId() + " has to wait since no memory block is available for allocation!");
                }
            }
        }
    }

    /**
     * Releases the memory held by a particular process
     *
     * @param pid
     */
    public static void releaseProcess(int pid) {
        if(processes.get(pid).isAllocated()) {
            Process p = processes.get(pid);
            if(p != null) {
                int blockId = p.getMemoryBlockIdAllocated();
                int index = -10;
                for(MemoryBlock mb: blocks) {
                    if(mb.getId() == blockId) {
                          index = blocks.indexOf(mb);
                          break;
                    }
                }
                if(index >= 0) {
                    int newSize = blocks.get(index).getSize() + p.getMemoryRequestSize();
                    blocks.get(index).setSize(newSize);
                    blocks.get(index).setAllocated(false);
                }
            }
        }
    }

    /**
     * Whether to release a process and if yes,
     * release the process and run algorithm again.
     *
     * @throws Exception
     */
    public static void release(int algo) throws Exception {
        System.out.println("Do you want to release any process?");
        System.out.println("Enter 1. for Yes and 2. for No");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int ch = Integer.parseInt(br.readLine());
        if(ch == 1) {
            System.out.print("Enter ID of process to release: ");
            int pid = Integer.parseInt(br.readLine());
            releaseProcess(pid);
            switch(algo) {
                case 1: doFirstFit();
                    break;
                case 2: doBestFit();
                    break;
                case 3: doWorstFit();
                    break;
            }
        }
    }

    /**
     * Default implementation to perform dynamic memory
     * allocation using worst fit algorithm.
     * The algorithm iterates over the list of processes
     * and does the following steps:
     *   1. Arrange the memory blocks in decreasing order of
     *      their size.
     *   2. For each process, do the following:
     *      2.1 Allocate the next largest block to this process
     *      2.2 Update process' PCB to indicate the memory block
     *          it is allocated.
     *      2.3 Update memory block's descriptor to indicate the
     *          process currently residing in it.
     *      2.4 Update size of the memory block =
     *              size of memory block - process' requested memory size
     */
    public static void doWorstFit() {
        blocks.sort(new Comparator<MemoryBlock>() {
            @Override
            public int compare(MemoryBlock o1, MemoryBlock o2) {
                return o2.getSize() - o1.getSize();
            }
        });
        for(int i = 0; i <= processes.size()-1; i++) {
            Process p = processes.get(i);
            if(!p.isAllocated()) {
                System.out.println("Searching for memory of size "
                        + p.getMemoryRequestSize() + " for process P" + p.getId());
                boolean flag = false;
                for(int j = 0; j < blocks.size(); j++) {
                    MemoryBlock mb = blocks.get(j);
                    if(!mb.isAllocated() && mb.getSize() >= p.getMemoryRequestSize()) {
                        mb.setAllocatedProcessId(p.getId());
                        p.setMemoryBlockIdAllocated(mb.getId());
                        p.setAllocated(true);
                        mb.setAllocated(true);
                        mb.setSize(mb.getSize() - p.getMemoryRequestSize());
                        System.out.println("Process P" + p.getId() + " allocated to memory block B"
                                + mb.getId());
                        System.out.println("New Partition created: B" + mb.getId()
                                + " of size: " + mb.getSize());
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    System.out.println("Process P" + p.getId() + " has to wait since no memory block is available for allocation!");
                }
            }
        }
    }

    /**
     * Default implementation to perform dynamic memory
     * allocation using best fit algorithm.
     * The algorithm iterates over the list of processes
     * and does the following steps:
     *   1. Arrange the memory blocks in increasing order of
     *      their size.
     *   2. For each process, do the following:
     *      2.1 Allocate the next optimum block to this process
     *      2.2 Update process' PCB to indicate the memory block
     *          it is allocated.
     *      2.3 Update memory block's descriptor to indicate the
     *          process currently residing in it.
     *      2.4 Update size of the memory block =
     *              size of memory block - process' requested memory size
     */
    public static void doBestFit() {
        blocks.sort(new Comparator<MemoryBlock>() {
            @Override
            public int compare(MemoryBlock o1, MemoryBlock o2) {
                return o1.getSize() - o2.getSize();
            }
        });
        for(int i = 0; i <= processes.size()-1; i++) {
            Process p = processes.get(i);
            if(!p.isAllocated()) {
                System.out.println("Searching for memory of size "
                        + p.getMemoryRequestSize() + " for process P" + p.getId());
                boolean flag = false;
                for(int j = 0; j < blocks.size(); j++) {
                    MemoryBlock mb = blocks.get(j);
                    if(!mb.isAllocated() && mb.getSize() >= p.getMemoryRequestSize()) {
                        mb.setAllocatedProcessId(p.getId());
                        p.setMemoryBlockIdAllocated(mb.getId());
                        p.setAllocated(true);
                        mb.setAllocated(true);
                        mb.setSize(mb.getSize() - p.getMemoryRequestSize());
                        System.out.println("Process P" + p.getId() + " allocated to memory block B"
                                + mb.getId());
                        System.out.println("New Partition created: B" + mb.getId()
                                + " of size: " + mb.getSize());
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    System.out.println("Process P" + p.getId() + " has to wait since no memory block is available for allocation!");
                }
            }
        }
    }

    /**
     * Prints memory status after algorithm is run.
     */
    public static void printDetails() {
        System.out.println("Dynamic Memory Allocation Details: ");
        System.out.println("------------------------------------");
        System.out.println("Block ID\tSize\tProcess Allocated");
        for(MemoryBlock mb: blocks) {
            if(mb.getAllocatedProcessId() >= 0) {
                System.out.print(mb.getId() + "\t\t" + mb.getSize() + "\t\t" + mb.getAllocatedProcessId());
                System.out.println();
            }
        }
    }
}

/**
 * A POJO class that denotes a process in
 * memory
 *
 * @author jimil
 */
class Process {
    int id;
    int memoryRequestSize;
    boolean isAllocated;
    int memoryBlockIdAllocated;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getMemoryRequestSize() {
        return memoryRequestSize;
    }
    public void setMemoryRequestSize(int memoryRequestSize) {
        this.memoryRequestSize = memoryRequestSize;
    }
    public boolean isAllocated() {
        return isAllocated;
    }
    public void setAllocated(boolean isAllocated) {
        this.isAllocated = isAllocated;
    }
    public int getMemoryBlockIdAllocated() {
        return memoryBlockIdAllocated;
    }
    public void setMemoryBlockIdAllocated(int memoryBlockIdAllocated) {
        this.memoryBlockIdAllocated = memoryBlockIdAllocated;
    }
}

/**
 * A POJO class that denotes a block in
 * memory.
 *
 * @author jimil
 */
class MemoryBlock {
    int id;
    int size;
    int allocatedProcessId;
    boolean isAllocated;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getAllocatedProcessId() {
        return allocatedProcessId;
    }
    public void setAllocatedProcessId(int allocatedProcessId) {
        this.allocatedProcessId = allocatedProcessId;
    }
    public boolean isAllocated() {
        return isAllocated;
    }
    public void setAllocated(boolean allocated) {
        isAllocated = allocated;
    }
}