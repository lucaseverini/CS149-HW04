/*
 ddeveloped by Arash
 */

import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class simulates the first come first served (FCFS) algorithm It receives
 * the arrayList with 100 processes that have already been initialized. It then
 * simulates the FCFS algorithm, and prints out results
 *
 * The first come first served algorithm sorts the array list based on time of
 * arrival
 *
 *
 */
public class FirstComeFirstServed {

    // instance variables - replace the example below with your own
    private ArrayList<Process> processArrayList;
    private ArrayList<Process> unsortedArrayList;
    private String oneSimulation;//OVERALL STRING REPRESENTATION
    private float averageWaitingTime;
    private float averageResponseTime;
    private float averageTurnaroundTime;
    private int throughput;

    /**
     * Constructor for objects of class FirstComeFirstServed
     *
     * @param processArrayList arrayList containing Process objects to endure
     * simulation
     */
    public FirstComeFirstServed(ArrayList<Process> processArrayList, ArrayList<Process> unsortedArrayList) {
        // initialise instance variables
        this.processArrayList = processArrayList;
        this.unsortedArrayList = unsortedArrayList;
        oneSimulation = "";
        averageWaitingTime = 0;
        averageResponseTime = 0;
        averageTurnaroundTime = 0;
    }

    public String simulateFCFS() {
        int numProcesses = 0;
        int quantum = 0;
        double timeRemaining;
        String timeChart = "";
        Process currentProcess = processArrayList.get(numProcesses);//store first process
        timeRemaining = currentProcess.getExpectedTime();
        boolean processRunning = true;
        boolean firstProcess = true;

        introduceProcess();//introduces processes that have been generated in advanced

        while (processRunning) {//as long as there are active processes, continue processing
            //if the current process has an arival time past than number of elapsed quantums, 
            //iterate quantum value
            if (currentProcess.getArrivalTime() > quantum) {
                quantum++;
                timeChart += "[---]";
            } else {
                if (firstProcess) {//first process needs to record start time
                    currentProcess.setStartTime(quantum);
                    firstProcess = false;
                }
                timeRemaining -= 1.0;//time remaining on current process decrements one 

                //if current process ends and still less than 100 quanta elapsed, start new process
                if (timeRemaining > 0 && quantum < 99 && !firstProcess) {
                    if ((quantum % 10) == 0) {//regarding formatting
                        timeChart += ("\n" + currentProcess.getName());//for printing out  chart of names
                    } else {
                        timeChart += currentProcess.getName();//for printing out chart of names
                    }

                } else if (timeRemaining < 0 && quantum < 99) {
                    if ((quantum % 10) == 0) {//regarding formatting
                        timeChart += ("\n" + currentProcess.getName());//for printing out  chart of names                        System.out.println("quantum: " + quantum + ",  process: " + processArrayList.get(numProcesses).getName() + "--quantum % 10 is true, timeRemaining < 0, quantum < 99");
                    } else {
                        timeChart += currentProcess.getName();//for printing out chart of names
                    }
                    currentProcess.setFinishTime(quantum + 1);//set  time value a process finishes

                    // put new process object into currentProcess
                    numProcesses++;
                    currentProcess = processArrayList.get(numProcesses);
                    currentProcess.setStartTime(quantum + 1);
                    // reset the time remaining to the replaced process's expected time value
                    timeRemaining = currentProcess.getExpectedTime();
                } else if (timeRemaining < 0 && quantum >= 99) {//for printing out the chart of names
                    if ((quantum % 10) == 0) {
                        timeChart += ("\n" + currentProcess.getName());
                    } else {
                        timeChart += currentProcess.getName();
                    }
                    currentProcess.setFinishTime(quantum + 1);//set the time value a process finishes
                    //if current process ends and is past 100 quanta, stop working
                    processRunning = false;
                }
                quantum++;
            }
            throughput = numProcesses;
        }
        oneSimulation += "Simulated order of First Come First Serve \n";
        oneSimulation += timeChart;
        oneSimulation += "\n" + getStringOfAverages(numProcesses);

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

        //generates the averages for each required statistic
        for (int i = 0; i < numProcesses; i++) {
            averageWaitingTime += processArrayList.get(i).getWaitingTime();
            averageResponseTime += processArrayList.get(i).getResponseTime();
            averageTurnaroundTime += processArrayList.get(i).getTurnaroundTime();
        }

        averageWaitingTime = averageWaitingTime / numProcesses;
        averageResponseTime = averageResponseTime / numProcesses;
        averageTurnaroundTime = averageTurnaroundTime / numProcesses;

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
