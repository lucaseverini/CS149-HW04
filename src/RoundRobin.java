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
public class RoundRobin {

    // instance variables
    private final ArrayList<Process> processArrayList;
    private final ArrayList<Process> unsortedArrayList;
    private final ArrayList<Process> processQueue;
    private final ArrayList<Process> processesDone;
    private String oneSimulation;//OVERALL STRING REPRESENTATION
    private float averageWaitingTime;
    private float averageResponseTime;
    private float averageTurnaroundTime;
    private int throughput;

    /**
     * Constructor for objects of class RoundRobin
     *
     * @param processArrayList arrayList containing Process objects to endure
     * simulation
     * @param unsortedArrayList arraylist representing unsorted processes
     */
    public RoundRobin(ArrayList<Process> processArrayList, ArrayList<Process> unsortedArrayList) {
        // initialise instance variables
        this.processArrayList = processArrayList;
        this.unsortedArrayList = unsortedArrayList;
        oneSimulation = "";
        averageWaitingTime = 0;
        averageResponseTime = 0;
        averageTurnaroundTime = 0;
        processQueue = new ArrayList<>();
        processesDone = new ArrayList<>();
    }

    public String simulateRR() {
        int finishedProcesses = 0;
        int startedProcesses = 0;
        int quantum = 0;
        String timeChart = "";
        boolean firstProcess = true;
        boolean processRunning = true;
        Process temporary;

        introduceProcess();

        //as long as active processes exist AND we have no processes that need attention
        while (processRunning && !(processQueue.isEmpty() && processArrayList.isEmpty())) {

            //if process Queue is empty, process Array List is NOT empty, and open for service
            if (processQueue.isEmpty() && !processArrayList.isEmpty() && quantum < 99) {
                processQueue.add(processArrayList.remove(0));
                while (processQueue.get(0).getArrivalTime() > quantum) {
                    quantum++;
                    timeChart += "[---]";
                }
            }

            //make sure anything that has arrived is put into processQueue
            if (quantum < 99) {
                while (!processArrayList.isEmpty() && processArrayList.get(0).getArrivalTime() < quantum) {
                    processQueue.add(processArrayList.remove(0));
                }
            }
            //not duplicate code, ensures program doesn't run for ever if not fed enough processes
            while (!processArrayList.isEmpty() && processArrayList.get(0).getArrivalTime() < quantum) {
                processQueue.add(processArrayList.remove(0));
            }

            //if its the first process, make sure start time = quantum
            if (!processQueue.isEmpty() && firstProcess) {
                if (!processQueue.get(0).getProcessStarted()) {
                    processQueue.get(0).setStartTime(quantum);
                    startedProcesses++;
                }
                firstProcess = false;
            }
            //run process
            if (!processQueue.isEmpty()) 
			{
                processQueue.get(0).runProcess();//timeRemaining -= 1;

                if (quantum < 99) {//If the simulation is active
                    if (!processQueue.get(0).getProcessStarted()) {
                        processQueue.get(0).setStartTime(quantum);
                        startedProcesses++;
                    }

                    if (processQueue.get(0).getTimeRemaining() > 0) {
                        if ((quantum % 10) == 0) {
                            timeChart += ("\n" + processQueue.get(0).getName());
                        } else {
                            timeChart += processQueue.get(0).getName();
                        }
                        temporary = processQueue.remove(0);
                        processQueue.add(temporary);
                        
                    } else if (processQueue.get(0).getTimeRemaining() < 0) {
                        if ((quantum % 10) == 0) {//regarding formatting
                            timeChart += ("\n" + processQueue.get(0).getName());
                        } else {
                            timeChart += processQueue.get(0).getName();
                        }
                        processQueue.get(0).setFinishTime(quantum + 1);
//                        lastProcessEnded = true;
                        finishedProcesses++;
                        temporary = processQueue.remove(0);
                        processesDone.add(temporary);
                    }
                } else if (quantum >= 99) {//Simulation becomes inactive, does not accept new processes
                    //remove any processes that have not been started
                    while (!processQueue.isEmpty() && !processQueue.get(0).getProcessStarted()) {
                        processQueue.remove(0);
                    }

                    if (!processQueue.isEmpty() && processQueue.get(0).getProcessStarted()) {
                        if (processQueue.get(0).getTimeRemaining() < 0) {
                            if ((quantum % 10) == 0) {
                                timeChart += ("\n" + processQueue.get(0).getName());
                            } else {
                                timeChart += processQueue.get(0).getName();
                            }
                            finishedProcesses++;
                            processQueue.get(0).setFinishTime(quantum + 1);
                            temporary = processQueue.remove(0);
                            processesDone.add(temporary);
                        } else if (processQueue.get(0).getTimeRemaining() > 0) {
                            if ((quantum % 10) == 0) {
                                timeChart += ("\n" + processQueue.get(0).getName());
                            } else {
                                timeChart += processQueue.get(0).getName();
                            }
                            temporary = processQueue.remove(0);
                            processQueue.add(temporary);
                        }
                    } else {
                        processRunning = false;
                        System.out.println("something broke!!!!!!!!!!");
                    }
                }
            }
            startedProcesses = processesDone.size();

            quantum++;
        }

        throughput = processesDone.size();

        oneSimulation += "Simulated order of Round Robin \n";
        oneSimulation += timeChart;

        oneSimulation += "\n" + getStringOfAverages(processesDone.size());

        return oneSimulation;//this is the OVERALL STRING REPRESENTATION
        //this will be sent to main, to be printed out along with all other 
        //OVERALL STRING REPRESENTATION's
    }

    /**
     * This processes the average statistics for one simulation of this
     * algorithm
     *
     * @param numProcesses the number of processes that started(were processed)
     * during simulation
     * @return 'averages' The string representing the averages to be attached to
     * FCFS's OVERALL STRING REPRESENTATION
     */
    public String getStringOfAverages(int numProcesses) {
        String averages = "";
        float totalWaitingTime = 0;
        float totalResponseTime = 0;
        float totalTurnaroundTime = 0;
        float waitingTime = 0;

        //generates the averages for each required statistic
        for (int i = 0; i < numProcesses; i++) {
            waitingTime = processesDone.get(i).getWaitingTime();
            if (waitingTime < 0) {
                System.out.println("negative!");
            }

            totalWaitingTime += waitingTime;//processesDone.get(i).getWaitingTime();
            totalResponseTime += processesDone.get(i).getResponseTime();
            totalTurnaroundTime += processesDone.get(i).getTurnaroundTime();
        }

        System.out.println();

        averageWaitingTime = totalWaitingTime / numProcesses;
        averageResponseTime = totalResponseTime / numProcesses;
        averageTurnaroundTime = totalTurnaroundTime / numProcesses;

        averages += "\nThe average Waiting time was: " + averageWaitingTime;
        averages += "\nThe average Response time was: " + averageResponseTime;
        averages += "\nThe average Turnaround time was: " + averageTurnaroundTime + "\n\n";

        return averages;
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
     * This returns an array with the statistics information
     *
     * @return averages
     */
    public float[] getStatistics() {
        float[] averages = { averageWaitingTime, averageResponseTime, averageTurnaroundTime, throughput};
        return averages;
    }

    public int RR(int y) {
        return y + y;
    }
}
