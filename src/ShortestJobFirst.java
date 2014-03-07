/*
 ddeveloped by Arash
 */

import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class simulates the Shortest Job First algorithm It receives the
 * arrayList with 100 processes that have already been initialized. It then
 * simulates the SJF algorithm, and prints out results
 *
 * The first come first served algorithm sorts the array list based on the time
 * remaining in each process that is active
 *
 *
 */
public class ShortestJobFirst {

    private ArrayList<Process> processArrayList;
    private ArrayList<Process> unsortedArrayList;
    private ArrayList<Process> processQueue;
    private ArrayList<Process> processesDone;
    private String oneSimulation;//OVERALL STRING REPRESENTATION
    private float averageWaitingTime;
    private float averageResponseTime;
    private float averageTurnaroundTime;
    private int throughput;

    /**
     * Constructor for objects of class ShortestJobFirst
     *
     * @param processArrayList arrayList containing Process objects to endure
     * simulation
     * @param unsortedArrayList arraylist representing unsorted processes
     */
    public ShortestJobFirst(ArrayList<Process> processArrayList, ArrayList<Process> unsortedArrayList) {
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

    public String simulateSJF() {
        int finishedProcesses = 0;
        int startedProcesses = 0;
        int quantum = 0;
        String timeChart = "";
        boolean lastProcessEnded = true;
        boolean processRunning = true;

        introduceProcess();

        //as long as active processes exist AND we have no processes that need attention
        while (processRunning && !(processQueue.isEmpty() && processArrayList.isEmpty())) {

            //if process Queue is empty, process Array List is NOT empty, and open for service
            if (processQueue.isEmpty() && quantum < 99 && !processArrayList.isEmpty()) {
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
            //This is not repeating code. previous lines finish program if the 
            // program is not fed enough processes to finish.      
            while (!processArrayList.isEmpty() && processArrayList.get(0).getArrivalTime() < quantum) {
                processQueue.add(processArrayList.remove(0));
            }

            //Sort processQueue based on expected time
            Collections.sort(processQueue, new Comparator<Process>() {
                @Override
                public int compare(Process process1, Process process2) {
                    if (process1.getExpectedTime() < process2.getExpectedTime()) {
                        return -1;
                    }
                    if (process1.getExpectedTime() > process2.getExpectedTime()) {
                        return +1;
                    }
                    return 0;
                }
            });

            //if the last process ended, start latest process
            //starts out as true for first iteration
            if (!processQueue.isEmpty() && lastProcessEnded) {
                if (!processQueue.get(0).getProcessStarted()) {
                    processQueue.get(0).setStartTime(quantum);
                    String name = processQueue.get(0).getName();
                    startedProcesses++;
                }
                lastProcessEnded = false;
            }

            //run process
            if (!processQueue.isEmpty()) {
                processQueue.get(0).runProcess();//timeRemaining -= 1;

                if (quantum < 99) {//If the simulation is active
                    if (!processQueue.isEmpty() && !processQueue.get(0).getProcessStarted()) {
                        processQueue.get(0).setStartTime(quantum);
                        String name = processQueue.get(0).getName();
                        startedProcesses++;
                    }

                    if (processQueue.get(0).getTimeRemaining() > 0) {
                        if ((quantum % 10) == 0) {
                            timeChart += ("\n" + processQueue.get(0).getName());
                        } else {
                            timeChart += processQueue.get(0).getName();
                        }
                    } else if (processQueue.get(0).getTimeRemaining() < 0) {
                        if ((quantum % 10) == 0) {//regarding formatting
                            timeChart += ("\n" + processQueue.get(0).getName());
                        } else {
                            timeChart += processQueue.get(0).getName();
                        }
                        processQueue.get(0).setFinishTime(quantum + 1);
                        lastProcessEnded = true;
                        finishedProcesses++;
                        processesDone.add(processQueue.remove(0));
                    }
                } else if (quantum >= 99) {//Simulation becomes inactive, does not accept new processes
                    lastProcessEnded = false;
                    if (processQueue.get(0).getProcessStarted()) {
                        if (processQueue.get(0).getTimeRemaining() < 0) {
                            if ((quantum % 10) == 0) {
                                timeChart += ("\n" + processQueue.get(0).getName());
                            } else {
                                timeChart += processQueue.get(0).getName();
                            }
                            finishedProcesses++;
                            processQueue.get(0).setFinishTime(quantum + 1);
                            processesDone.add(processQueue.remove(0));
                        } else if (processQueue.get(0).getTimeRemaining() > 0) {
                            if ((quantum % 10) == 0) {
                                timeChart += ("\n" + processQueue.get(0).getName());
                            } else {
                                timeChart += processQueue.get(0).getName();
                            }
                        }
                    } else if (!processQueue.get(0).getProcessStarted()) {
                        processRunning = false;
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

        oneSimulation += "Simulated order of Shortest Job First \n";
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
        float[] averages = {averageWaitingTime, averageResponseTime, averageTurnaroundTime, throughput};
        return averages;
    }

    /**
     * This method will be replaced by a class that will be used in the main
     * class to print everything to a single file all at once.
     *
     * @param text
     */
    public void displayProcess(String text) {

        try {
            File file = new File("test", "newFile.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            //parameter 'content' is content to be written to file
            //should probably store in a string, then put here.
            bw.write(text);
            bw.close();
            System.out.println("\nFirst Come First Serve has been printed to file. \n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
