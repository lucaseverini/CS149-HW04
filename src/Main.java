/*
 * @author Arash Zahoory, Luca Severini, Romeo Stevens
 */

import java.util.*;

/**
 * This is the main class of the swapping and paging simulation algorithms
 *
 */
public class Main {
    public static final int SIMULATIONS = 5;
    public static final int PROCESSES = 100;
    public static final int QUANTA = 60;

    public static void main(String[] args) {
        printToFile printer = new printToFile();
        String simulationString = "";
        String totalFile = "";
        String swapping;
//        String RR;
//        String FCFS;
//        String SJF;
//        String SRT;

//        FCFS = runFCFS();
//        totalFile += FCFS;
//       
        swapping = runSwapping(1); //run first fit
        totalFile += swapping;
        swapping = runSwapping(2); //run next fit
        totalFile += swapping;
        swapping = runSwapping(3); //run best fit
        totalFile += swapping;

//        RR = runRR();
//        totalFile += RR;
//
//        SJF = runSJF();
//        totalFile += SJF;
//        SRT = runSRT();
//        totalFile += SRT;
        printer.printToFile(totalFile);
    }

//
//    public static String runFCFS() {
//        ArrayList<Process> processArrayList;
//        ArrayList<Process> unsortedArrayList;
//        String simulationString;
//        String totalFile = "";
//        float[] statistics;
//        float averageWaitingTime = 0;
//        float averageTurnaroundTime = 0;
//        float averageResponseTime = 0;
//        float throughput = 0;
//        float totalAverageWaitingTime;
//        float totalAverageTurnaroundTime;
//        float totalAverageResponseTime;
//        float totalThroughput;
//        int i;
//
//        // ProcessGenerator: first parameter is number of processes to generate
//        // second parameter is seed number for random function.
//        ProcessGenerator newProcesses;
//
//        FirstComeFirstServed FCFS;
//
//        //Running 5 simulations, and adding results to 'totalFile' to be printed out
//        for (i = 0; i < SIMULATIONS; i++) {
//            newProcesses = new ProcessGenerator(PROCESSES, i);
//            processArrayList = newProcesses.generateProcesses();
//            unsortedArrayList = newProcesses.getUnsortedArrayList();
//
//            FCFS = new FirstComeFirstServed(processArrayList, unsortedArrayList);
//            simulationString = FCFS.simulateFCFS();
//
//            statistics = FCFS.getStatistics();
//            if (i == 0) {
//                totalFile += "---------------------------------------------------------------------------------------";
//            }
//            totalFile += "\nSimulation #" + (i + 1) + " of First Come First Served: \n";
//            totalFile += simulationString;
//
//            averageWaitingTime += statistics[0];
//            averageResponseTime += statistics[2];
//            averageTurnaroundTime += statistics[1];
//            throughput += statistics[3];
//        }
//
//        totalAverageWaitingTime = averageWaitingTime / i;
//        totalAverageResponseTime = averageResponseTime / i;
//        totalAverageTurnaroundTime = averageTurnaroundTime / i;
//        totalThroughput = throughput / (float) i;
//
//        totalFile += "\nTotal Average Waiting Time for FCFS was: " + totalAverageWaitingTime;
//        totalFile += "\nTotal Average Response Time for FCFS was: " + totalAverageResponseTime;
//        totalFile += "\nTotal Average Turnaround for FCFS was: " + totalAverageTurnaroundTime;
//        totalFile += "\nTotal Average Throughput for FCFS was: " + totalThroughput + "\n\n";
//
//        return totalFile;
//    }
//    public static String runRR() {
//        ArrayList<Process> processArrayList;
//        ArrayList<Process> unsortedArrayList;
//
//        String simulationString;
//        String totalFile = "";
//        float[] statistics;
//        float averageWaitingTime = 0;
//        float averageTurnaroundTime = 0;
//        float averageResponseTime = 0;
//        float throughput = 0;
//        float totalAverageWaitingTime;
//        float totalAverageTurnaroundTime;
//        float totalAverageResponseTime;
//        float totalThroughput;
//        int i;
//
//        // ProcessGenerator: first parameter is number of processes to generate
//        // second parameter is seed number for random function.
//        ProcessGenerator newProcesses;
//
//        RoundRobin RR;
//
//        //Running 5 simulations, and adding results to 'totalFile' to be printed out
//        for (i = 0; i < SIMULATIONS; i++) {
//            newProcesses = new ProcessGenerator(PROCESSES, i);
//            processArrayList = newProcesses.generateProcesses();
//            unsortedArrayList = newProcesses.getUnsortedArrayList();
//
//            RR = new RoundRobin(processArrayList, unsortedArrayList);
//            simulationString = RR.simulateRR();
//            statistics = RR.getStatistics();
//            if (i == 0) {
//                totalFile += "---------------------------------------------------------------------------------------";
//
//            }
//            totalFile += "\nSimulation #" + (i + 1) + " of Round Robin: \n";
//            totalFile += simulationString;
//
//            averageWaitingTime += statistics[0];
//            averageResponseTime += statistics[2];
//            averageTurnaroundTime += statistics[1];
//            throughput += statistics[3];
//        }
//
//        totalAverageWaitingTime = averageWaitingTime / i;
//        totalAverageResponseTime = averageResponseTime / i;
//        totalAverageTurnaroundTime = averageTurnaroundTime / i;
//        totalThroughput = throughput / (float) i;
//
//        totalFile += "\nTotal Average Waiting Time for RR was: " + totalAverageWaitingTime;
//        totalFile += "\nTotal Average Response Time for RR was: " + totalAverageResponseTime;
//        totalFile += "\nTotal Average Turnaround for RR was: " + totalAverageTurnaroundTime;
//        totalFile += "\nTotal Average Throughput for RR was: " + totalThroughput + "\n\n";
//
//        return totalFile;
//    }
    
    
    /**
     * This simulates the swapping algorithm
     *
     * @param type the type of simulation being run
     * @return 'oneSimulation' The string representing the simulation
     */
    public static String runSwapping(int type) {
        ArrayList<Process> processArrayList;
        ArrayList<Process> unsortedArrayList;

        String simulationString;
        String totalFile = "";

        float throughput = 0;
        float averageThroughput;
        int i;

        // ProcessGenerator: first parameter is number of processes to generate
        // second parameter is seed number for random function.
        ProcessGenerator newProcesses;

        Swapping swapping;

        //Running 5 simulations, and adding results to 'totalFile' to be printed out
        for (i = 0; i < SIMULATIONS; i++) {
            newProcesses = new ProcessGenerator(PROCESSES, i);
            processArrayList = newProcesses.generateProcesses();
            unsortedArrayList = newProcesses.getUnsortedArrayList();

            swapping = new Swapping(processArrayList, unsortedArrayList, QUANTA, type);
            simulationString = swapping.simulateSwapping();
            if (i == 0) {
                totalFile += "---------------------------------------------------------------------------------------";

            }
            if (type == 1) {
                totalFile += "\nSimulation #" + (i + 1) + " of Swapping First Fit: \n";
            }else if (type == 2) {
                totalFile += "\nSimulation #" + (i + 1) + " of Swapping Next Fit: \n";
            }else if (type == 3) {
                totalFile += "\nSimulation #" + (i + 1) + " of Swapping Best Fit: \n";
            }
            totalFile += simulationString;

            throughput += swapping.getThroughput();
        }

        averageThroughput = throughput / (float) i;

        totalFile += "\nTotal Average Throughput for Swapping was: " + averageThroughput + "\n\n";

        return totalFile;
    }

//    public static String runSJF() {
//        ArrayList<Process> processArrayList;
//        ArrayList<Process> unsortedArrayList;
//        String simulationString;
//        String totalFile = "";
//        float[] statistics;
//        float averageWaitingTime = 0;
//        float averageTurnaroundTime = 0;
//        float averageResponseTime = 0;
//        float throughput = 0;
//        float totalAverageWaitingTime;
//        float totalAverageTurnaroundTime;
//        float totalAverageResponseTime;
//        float totalThroughput;
//        int i;
//
//        // ProcessGenerator: first parameter is number of processes to generate
//        // second parameter is seed number for random function.
//        ProcessGenerator newProcesses;
//
//        ShortestJobFirst SJF;
//
//        //Running 5 simulations, and adding results to 'totalFile' to be printed out
//        for (i = 0; i < SIMULATIONS; i++) {
//            newProcesses = new ProcessGenerator(PROCESSES, i);
//            processArrayList = newProcesses.generateProcesses();
//            unsortedArrayList = newProcesses.getUnsortedArrayList();
//
//            SJF = new ShortestJobFirst(processArrayList, unsortedArrayList);
//            simulationString = SJF.simulateSJF();
//            statistics = SJF.getStatistics();
//            if (i == 0) {
//                totalFile += "---------------------------------------------------------------------------------------";
//            }
//            totalFile += "\nSimulation #" + (i + 1) + " of Shortest Job First: \n";
//            totalFile += simulationString;
//
//            averageWaitingTime += statistics[0];
//            averageResponseTime += statistics[2];
//            averageTurnaroundTime += statistics[1];
//            throughput += statistics[3];
//        }
//
//        totalAverageWaitingTime = averageWaitingTime / i;
//        totalAverageResponseTime = averageResponseTime / i;
//        totalAverageTurnaroundTime = averageTurnaroundTime / i;
//        totalThroughput = throughput / (float) i;
//
//        totalFile += "\nTotal Average Waiting Time for SJF was: " + totalAverageWaitingTime;
//        totalFile += "\nTotal Average Response Time for SJF was: " + totalAverageResponseTime;
//        totalFile += "\nTotal Average Turnaround for SJF was: " + totalAverageTurnaroundTime;
//        totalFile += "\nTotal Average Throughput for SJF was: " + totalThroughput + "\n\n";
//
//        return totalFile;
//    }
//
//    public static String runSRT() {
//        ArrayList<Process> processArrayList;
//        String simulationString;
//        String totalFile = "";
//        float[] statistics;
//        float averageWaitingTime = 0;
//        float averageTurnaroundTime = 0;
//        float averageResponseTime = 0;
//        float throughput = 0;
//        float totalAverageWaitingTime;
//        float totalAverageTurnaroundTime;
//        float totalAverageResponseTime;
//        float totalThroughput;
//
//        for (int idx = 0; idx < SIMULATIONS; idx++) {
//            ProcessGenerator procGen = new ProcessGenerator(PROCESSES, idx);
//            processArrayList = procGen.generateProcesses();
//
//            ShortestRemainingTime SRT = new ShortestRemainingTime(processArrayList);
//
//            simulationString = SRT.simulatePreemptive(QUANTA);
//
//            statistics = SRT.getStatistics();
//            if (idx == 0) {
//                totalFile += "---------------------------------------------------------------------------------------";
//            }
//            totalFile += "\nSimulation #" + (idx + 1) + " of Shortest Remaining  Time (SRT): \n";
//            totalFile += simulationString;
//
//            averageWaitingTime += statistics[0];
//            averageResponseTime += statistics[2];
//            averageTurnaroundTime += statistics[1];
//            throughput += statistics[3];
//        }
//
//        totalAverageWaitingTime = averageWaitingTime / SIMULATIONS;
//        totalAverageResponseTime = averageResponseTime / SIMULATIONS;
//        totalAverageTurnaroundTime = averageTurnaroundTime / SIMULATIONS;
//        totalThroughput = throughput / SIMULATIONS;
//
//        totalFile += "\nTotal Average Waiting Time for SRT was: " + totalAverageWaitingTime;
//        totalFile += "\nTotal Average Response Time for SRT was: " + totalAverageResponseTime;
//        totalFile += "\nTotal Average Turnaround for SRT was: " + totalAverageTurnaroundTime;
//        totalFile += "\nTotal Average Throughput for SRT was: " + totalThroughput + "\n\n";
//
//        return totalFile;
//    }
}
