/*
 *Developed by Romeo
 */

import java.util.*;

/**
 * Round Robin is preemptive Round Robin completes high priority jobs before low
 * priority ones
 *
 * @RomeoStevens
 */
public class Swapping {

    // instance variables
    private final ArrayList<Process> processArrayList;
    private final ArrayList<Process> unsortedArrayList;
    private final ArrayList<Process> processQueue;
    private final ArrayList<Process> processesInMemory;
    private final ArrayList<Process> processesDone;
    private ArrayList<Gaps> gapList;
    private final MegaByte[] memory;
    private String currentMemoryArray;//OVERALL STRING REPRESENTATION
    private String initialMemoryArray;//starts out as empty: ...........
    private String oneSimulation;
    private int throughput;
    private boolean leave;

    /**
     * Constructor for objects of class Swapping
     *
     * @param processArrayList arrayList containing Process objects to endure
     * simulation
     * @param unsortedArrayList arraylist representing unsorted processes
     */
    public Swapping(ArrayList<Process> processArrayList, ArrayList<Process> unsortedArrayList) {
        // initialise instance variables
        memory = new MegaByte[100];
        this.processArrayList = processArrayList;
        this.unsortedArrayList = unsortedArrayList;
        processQueue = new ArrayList<>();
        processesInMemory = new ArrayList<>();
        processesDone = new ArrayList<>();
        gapList = new ArrayList<>();
        MegaByte megaByte;
        currentMemoryArray = "";
        initialMemoryArray = "";
        oneSimulation = "";

        //setting up the process map as empty: .............., and so on
        for (int i = 0; i < memory.length; i++) {
            megaByte = new MegaByte();
            memory[i] = megaByte;
            if (i != 0 && (i % 20) == 0) {
                initialMemoryArray += "\n" + memory[i].getMegaByte();
            } else {
                initialMemoryArray += memory[i].getMegaByte();
            }
        }
        //gapList has one gap object, that 'fills' whole arraylist
        Gaps gap = new Gaps();
        gapList.add(gap);
        //System.out.println(startSimulation);
    }

    /**
     *
     * @return
     */
    public String simulateSwapping() {

        int quantum = 0;
        String timeChart = "";

        //System.out.println(oneSimulation);
        introduceProcess();

        //gapList = getGaps();
        //as long as open AND (processQueue and processArrayList) arent empty at same time
        while (quantum < 60 && !(processQueue.isEmpty() && processArrayList.isEmpty())) {
            System.out.println("\n1\n");

            //remove processes that have ended //# 2, 3, 4
            if (!processesInMemory.isEmpty()) {
                System.out.println("\n2\n");

                for (int rCounter = 0; rCounter < processesInMemory.size(); rCounter++) {
                    System.out.println("\n3\n");

                    if (processesInMemory.get(rCounter).getTimeRemaining() <= 0) {
                        removeFromMemory(processesInMemory.get(rCounter).getName());
                        processesDone.add(processesInMemory.remove(0));
                        System.out.println("\n4\n");
                    }
                }
            }//gaps from process that ended before this quantum starts are empty 

            //if process Queue is empty, process ArrayList is NOT empty
            //put first element of processArrayList into processQueue and 'wait' until it has 'arrived'
            //# 5, 6
            if (processQueue.isEmpty() && !processArrayList.isEmpty()) {
                System.out.println("\n5\n");

                processQueue.add(processArrayList.remove(0));
                while (quantum < 60 && processQueue.get(0).getArrivalTime() > quantum) {
                    System.out.println("\n6\n");
                    quantum++;
                    timeChart += "Minute " + quantum;
                }
            }//processQueue has an element, and it has arrived

            //anything in processArrayList that has arrived -> goes into processQueue
            while (!processArrayList.isEmpty() && processArrayList.get(0).getArrivalTime() < quantum) {
                System.out.println("\n7\n");
                processQueue.add(processArrayList.remove(0));
            }//now we have a processQueue that has all processes that have 'arrived'

            //(start(put in memory) any process that has arrived and fits)
            //for every process in the processQueue, in order of arrival:
            //check if can fit-> yes: place into memory, no: wait until next minute/second
            //# 8, 9, 
            
            boolean foundGap;// = false; 
            
            leave = false;
            for (int pCounter = 0; pCounter < processQueue.size(); pCounter++) {
                foundGap = false; 
                for (int gapCounter = 0; gapCounter < gapList.size(); gapCounter++) {
                    System.out.println("\n8\n");
                    if (processQueue.get(0).getProcessSize() <= gapList.get(gapCounter).getGapSize()) {
                        System.out.println("\n9\n");
                        //place process into memory that it fits in,                 manipulate currentMemoryArray
                        int gapLocation = gapList.get(gapCounter).getGapLocation();
                        String processName = processQueue.get(0).getName();
                        int processSize = processQueue.get(0).getProcessSize();

                        addToMemory(gapLocation, processSize, processName);
                        //print to text
                        timeChart += "Minute " + quantum + ":\n" + currentMemoryArray + "\n";
                        //set start time of process added
                        processQueue.get(0).setStartTime(quantum);
                        //remove from queue, add to memory arrayList
                        processesInMemory.add(processQueue.remove(0));
                        foundGap = true; 
                        break;
                    } //else {
                        //leave = true;
                        //break;
                    //}
                }//process in processQueue has been placed if space in memory exists
//                if (leave) {
//                    break;
//                }
                if (foundGap == false)
                    break; 
            }

            //run processes that are active
            if (!processesInMemory.isEmpty()) {
//                System.out.println("\n10\n");
                for (int runningCounter = 0; runningCounter < processesInMemory.size(); runningCounter++) {
//                    System.out.println("\n11\n");
                    processesInMemory.get(0).runProcess();//timeRemaining -= 1;
                }
            }
            quantum++;
//            System.out.println("\n12\n");
        }
//        System.out.println("\n13\n");
        oneSimulation += timeChart;

        return oneSimulation;//this is the OVERALL STRING REPRESENTATION
        //this will be sent to main, to be printed out along with all other 
        //OVERALL STRING REPRESENTATION's
    }

    /**
     * This sets up a string value for all the process objects
     *
     */
    public void introduceProcess() {
        String content = "";
        for (int i = 0; i < unsortedArrayList.size(); i++) {
            content += unsortedArrayList.get(i).toString();
        }
        //displayProcess(content);//for testing purposes
        oneSimulation += "\n" + content + "\n"; //adds to simulation's OVERALL STRING REPRESENTATION
        System.out.println(oneSimulation);
    }

    /**
     * This returns number of activated processes
     *
     * @return averages
     */
    public int getThroughput() {

        throughput = processesInMemory.size() + processesDone.size();
        return throughput;

    }

    /**
     * This returns number of activated processes
     *
     * @return averages
     */
    //go through memory[100] and return an array list of gap objects
    //Gaps(int gapSize, int gapLocation)
    private ArrayList<Gaps> getGaps() {
        ArrayList<Gaps> arrayGaps = new ArrayList<>();
        Gaps oneGap;
        int gapSize = 0;
        int gapPosition;
//        boolean first = true;

        System.out.println("\ngap1\n");
        System.out.println("");
        for (int idx = 0; idx < memory.length; idx++) {
//            System.out.println("\ngap2\n");

            if (memory[idx].getMegaByte().equals("[__]")) {
//                System.out.println("\ngap3\n");

                gapSize++;
            } else {
//                System.out.println("\ngap4\n");

                gapPosition = idx - gapSize;
                oneGap = new Gaps(gapSize, gapPosition);
                arrayGaps.add(oneGap);
                gapSize = 0;
            }
        }
//        System.out.println("\ngap5\n");

//        if(first){
//            first = false;
//            oneGap = new Gaps(100, 0);
//            arrayGaps.add(oneGap);
//            return arrayGaps;
//        }else{
//            return arrayGaps;
//        }
        return arrayGaps;
    }

//not the gap size, the size of the process
    private void addToMemory(int gapLocation, int processSize, String processName) {
        MegaByte mByte;

        for (int idx = gapLocation; idx < (gapLocation + processSize); idx++) {
            mByte = new MegaByte(processName);
            memory[idx] = mByte;
        }

        gapList = getGaps();

        memoryToString();
    }

    private void removeFromMemory(String name) {
        MegaByte mByte;

        for (int idx = 0; idx < memory.length; idx++) {
            if (memory[idx].getMegaByte().equals(name)) {
                mByte = new MegaByte();
                memory[idx] = mByte;
            }
        }

        gapList = getGaps();

        memoryToString();
    }

    private void memoryToString() {
        for (int i = 0; i < memory.length; i++) {
            if (i != 0 && (i % 20) == 0) {
                currentMemoryArray += "\n" + memory[i].getMegaByte();
            } else {
                currentMemoryArray += memory[i].getMegaByte();
            }
        }
    }

}
