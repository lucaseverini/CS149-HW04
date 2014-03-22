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

    public static void main(String[] args) 
	{
        printToFile printer = new printToFile();
        String simulationString = "";
        String totalFile = "";
        String swapping;

		swapping = runSwapping(1); //run first fit
        totalFile += swapping;
        swapping = runSwapping(2); //run next fit
        totalFile += swapping;
        swapping = runSwapping(3); //run best fit
        totalFile += swapping;
		
		Paging paging = new Paging();	
		totalFile += paging.runPaging(SIMULATIONS, PROCESSES);

		printer.printToFile(totalFile);
		
		System.out.println("Done.");
    }
    
    /**
     * This simulates the swapping algorithm
     *
     * @param type the type of simulation being run
     * @return 'oneSimulation' The string representing the simulation
     */
    public static String runSwapping(int type) 
	{
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
}
