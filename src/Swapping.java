/*
 * @author Arash Zahoory, Luca Severini, Romeo Stevens
 */

import java.util.*;

/**
 * Swapping is a class that implements a simulation of swapping different sized
 * processes into and out of memory during a specified duration of time.
 *
 */
public class Swapping {

    // instance variables
    private final ArrayList<Process> processArrayList;
    private final ArrayList<Process> unsortedArrayList;
    private final ArrayList<Process> processQueue;
    private ArrayList<Process> processesInMemory;
    private ArrayList<Process> processesDone;
    private ArrayList<Process> tempProcess;
    private ArrayList<Process> tempDoneProcess;
    private ArrayList<Gaps> gapList;
    private final MegaByte[] memory;
    private String currentMemoryArray;//string representation of current array
    private String initialMemoryArray;//starts out as empty: ...........
    private String oneSimulation;
    private int throughput;
    private final int QUANTA;
    private boolean bestFit;
    private boolean nextFit;
    private boolean firstFit;
    private int endOfLastProcess;

    /**
     * Constructor for objects of class Swapping
     *
     * @param processArrayList arrayList containing Process objects to endure
     * simulation
     * @param unsortedArrayList arrayList representing unsorted processes
     * @param QUANTA number of cycles
     * @param type the type of the swapping algorithm: first fit, next fit, best
     * fit
     */
    public Swapping(ArrayList<Process> processArrayList, ArrayList<Process> unsortedArrayList, int QUANTA, int type) {
        // initialise instance variables
        memory = new MegaByte[100];
        this.processArrayList = processArrayList;
        this.unsortedArrayList = unsortedArrayList;
        processQueue = new ArrayList<>();
        processesInMemory = new ArrayList<>();
        processesDone = new ArrayList<>();
        gapList = new ArrayList<>();
        tempProcess = new ArrayList<>();
        MegaByte megaByte;
        currentMemoryArray = "";
        initialMemoryArray = "";
        oneSimulation = "";
        this.QUANTA = QUANTA;
        bestFit = false;
        nextFit = false;
        firstFit = false;
        endOfLastProcess = 0;

        switch (type) {
            case 1: {
                firstFit = true;//run first fit
                break;
            }
            case 2: {
                nextFit = true;//run next fit
                break;
            }
            case 3: {
                bestFit = true;//run best fit
                break;
            }

        }

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
    }

    /**
     * This simulates the swapping algorithm
     *
     * @return 'oneSimulation' The string representing the simulation
     */
    public String simulateSwapping() {
        int quantum;

        introduceProcess();

        for (quantum = 0; quantum < QUANTA; quantum++) {
            //if process Queue is empty, and process ArrayList is NOT empty, and first in pArrayList hasnt arrived
            //check if first element has arrived yet or not, 
            //if not arrived, continue
            if (!processArrayList.isEmpty() && processQueue.isEmpty() && processArrayList.get(0).getArrivalTime() > quantum) {
                removeFinishedProcesses(quantum);
            } else {
                //remove processes that have ended 
                removeFinishedProcesses(quantum);
                //place anything in processArrayList that has arrived -> goes into processQueue
                while (!processArrayList.isEmpty() && processArrayList.get(0).getArrivalTime() <= quantum) {
                    processQueue.add(processArrayList.remove(0));
                }//now we have a processQueue that has all processes that have 'arrived'

                boolean foundGap;// = false; 
                //add to memory the processes that fit
                while (processQueue.size() > 0) {
                    foundGap = false;
                    for (int gapCounter = 0; gapCounter < gapList.size(); gapCounter++) {
                        if (processQueue.get(0).getProcessSize() <= gapList.get(gapCounter).getGapSize()) {
                            //place process into memory that it fits in,                 manipulate currentMemoryArray
                            int gapLocation = gapList.get(gapCounter).getGapLocation();
                            String processName = processQueue.get(0).getName();
                            int processSize = processQueue.get(0).getProcessSize();
                            addToMemory(gapLocation, processSize, processName, quantum);
                            //set start time of process added
                            processQueue.get(0).setStartTime(quantum);
                            //remove from queue, add to memory arrayList
                            processesInMemory.add(processQueue.remove(0));
                            foundGap = true;
                            break;
                            //inside for loop
                        }
                        // inside for loop
                    }
                    if (foundGap == false) {
                        break;
                    }
                }//fit processes added to memory
            }//end of else

            //Processes in memory must run 
            //each has an expected run time/life.  
            //Here we count down one minute/quanta  for every cycle that passes
            if (!processesInMemory.isEmpty()) {
                for (int runningCounter = 0; runningCounter < processesInMemory.size(); runningCounter++) {
                    processesInMemory.get(runningCounter).runProcess();//timeRemaining -= 1;
                }
            }
        }

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
        oneSimulation += "\n" + content + "\n"; //adds to simulation's OVERALL STRING REPRESENTATION
    }

    /**
     * This returns number of activated processes
     *
     * @return throughput
     */
    public int getThroughput() {

        throughput = processesInMemory.size() + processesDone.size();
        return throughput;

    }

    /**
     * This updates the list of gap objects representing gaps in memory
     *
     * @return arrayGaps, an updated Array List of gap processes
     */
    private ArrayList<Gaps> getGaps() {
        ArrayList<Gaps> arrayGaps = new ArrayList<>();
        Gaps oneGap;
        int gapSize = 0;
        int gapPosition;
        boolean gapHasStarted = false;

        for (int idx = 0; idx < memory.length + 1; idx++) {
            //finish off a gap that has ended at element 100
            if (idx == memory.length && gapHasStarted) {
                gapPosition = idx - gapSize;
                oneGap = new Gaps(gapSize, gapPosition);
                arrayGaps.add(oneGap);
            } else if (idx == memory.length && !gapHasStarted) {
            } else if (memory[idx].isFull() && !gapHasStarted) {
            } else if (!memory[idx].isFull() && !gapHasStarted) {
                //start a gap when one hasn't been started
                gapSize++;
                gapHasStarted = true;
            } else if (gapHasStarted && !memory[idx].isFull()) {
                //continue a gap
                gapSize++;
            } else if (gapHasStarted && memory[idx].isFull()) {
                gapPosition = idx - gapSize;
                oneGap = new Gaps(gapSize, gapPosition);
                arrayGaps.add(oneGap);
                gapSize = 0;
                gapHasStarted = false;
            }
        }

        //if best fit is used, sort gaps by smallest first
        if (bestFit) {
            Collections.sort(arrayGaps, new Comparator<Gaps>() {
                @Override
                public int compare(Gaps gap1, Gaps gap2) {
                    if (gap1.getGapSize() < gap2.getGapSize()) {
                        return -1;
                    }
                    if (gap1.getGapSize() > gap2.getGapSize()) {
                        return +1;
                    }
                    return 0;
                }
            });
        }

        int nextFitCounter = 0;

        if (nextFit) {
            while (nextFitCounter < (arrayGaps.size()) && arrayGaps.get(0).getGapLocation() < endOfLastProcess) {
                nextFitCounter++;
                arrayGaps.add(arrayGaps.remove(0));
            }
        }
        return arrayGaps;
    }

    /**
     * This adds a process to memory
     *
     * @param gapLocation the location in the size 100 memory array where the
     * process will be added
     * @param processSize the size of the process to be added
     * @param processName the name of the process to be added
     * @param quantum the quantum/minute in which the process
     */
    private void addToMemory(int gapLocation, int processSize, String processName, int quantum) {
        MegaByte mByte;

        for (int idx = gapLocation; idx < (gapLocation + processSize); idx++) {
            mByte = new MegaByte(processName);
            memory[idx] = mByte;
        }

        endOfLastProcess = gapLocation + processSize;
        gapList = getGaps();
        memoryToString();

        oneSimulation += "\nMinute: " + quantum + "\nProcess Added: " + processName + "\n"
                + currentMemoryArray + "\n";
    }

    /**
     * This removes a process from memory
     * 
     * @param name the name of the process that is being removed
     * @param quantum the quantum/minute in which this is occuring
     */
    private void removeFromMemory(String name, int quantum) {
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

    /**
     * This removes a process from memory if it has finished
     * 
     * @param quantum the quantum/minute in which this is happening
     */
    private void removeFinishedProcesses(int quantum) {
        //remove processes that have ended //# 2, 3, 4
        if (!processesInMemory.isEmpty()) {
            for (int rCounter = 0; rCounter < processesInMemory.size(); rCounter++) {
                if (processesInMemory.get(rCounter).getTimeRemaining() <= 0) {
                    removeFromMemory(processesInMemory.get(rCounter).getName(), quantum);//just manipulates array
                    oneSimulation += "\nMinute: " + quantum + "\nProcess Removed: " + processesInMemory.get(rCounter).getName() + "\n"
                            + currentMemoryArray + "\n";
                    processesInMemory.get(rCounter).shouldRemove();
                }
            }

//              split processes in memory array list into done and not done
            tempProcess = new ArrayList<>();
            tempDoneProcess = new ArrayList<>();
            for (int rCounter = 0; rCounter < processesInMemory.size(); rCounter++) {
                if (processesInMemory.get(rCounter).getShouldRemove()) {
                    tempDoneProcess.add(processesInMemory.get(rCounter));
                } else {
                    tempProcess.add((processesInMemory.get(rCounter)));
                }
            }

            processesDone = new ArrayList<>();
            int tempDoneProc = tempDoneProcess.size();
            for (int rCounter = 0; rCounter < tempDoneProc; rCounter++) {
                processesDone.add(tempDoneProcess.remove(0));
            }

            processesInMemory = new ArrayList<>();
            int tempProc = tempProcess.size();
            for (int rCounter = 0; rCounter < tempProc; rCounter++) {
                processesInMemory.add(tempProcess.remove(0));
            }

        }//gaps from process that ended before this quantum starts are empty 

    }

    /**
     * This converts the memory array into a string object so it can be printed to file
     * 
     */
    private void memoryToString() {

        currentMemoryArray = "";
        for (int i = 0; i < memory.length; i++) {
            if (i != 0 && (i % 20) == 0) {
                currentMemoryArray += "\n" + memory[i].getMegaByte();
            } else {
                currentMemoryArray += memory[i].getMegaByte();
            }
        }
    }
}
