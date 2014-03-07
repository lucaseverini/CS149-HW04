/*
*	ShortestRemainingTime.java
*
*   Assignment #2 - CS149 - SJSU
*
*	By Luca Severini (lucaseverini@mac.com)
* 
*	San Jose Feb-25-2014
*/

import java.util.*;

/**
 * This class implements the SRT (Shortest Remaining Time) scheduling algorithm
 */
public class ShortestRemainingTime
{
	private int quantum;
	private final ArrayList<Process> processList;
    private ArrayList<Process> sortedProcessList;
	private ArrayList<Process> runningProcessList;
	private ArrayList<Process> processesDone;
	private String oneSimulation;
    private float averageWaitingTime;
    private float averageResponseTime;
    private float averageTurnaroundTime;
    private int throughput;

    /**
     * Constructor for objects of class ShortestRemainingTime
	 * @param processArrayList
	 */
    public ShortestRemainingTime(ArrayList<Process> processArrayList)
    {
		this.processList = processArrayList;
    }
	
	/**
	 * Simulation of preemptive SRT
	 * @param totQuanta
	 * @return
	 */
	public String simulatePreemptive(int totQuanta) 
	{
		String timeChart = "";
		
		introduceProcess();
		
		sortedProcessList = processList;
		sortProcessesByArrivalTime(sortedProcessList);
		
		runningProcessList = new ArrayList<>();
		processesDone = new ArrayList<>();
/*		
		printProcessList(sortedProcessList);
		System.out.println();
*/ 
		Process nextProcess = null;
 		Process currentProcess = null;
 		int processIdx = 0;
        quantum = 0;
		 
		System.out.println("Simulation running for " + totQuanta + " quanta...");
		
		// Run loop for some quanta...
        while (quantum < totQuanta) 
		{
			System.out.println("quantum " + quantum + " :");
			
			// Add the processes to the running list as they become arrive...
			boolean processesAdded = false;
			if(nextProcess == null && processIdx < sortedProcessList.size())
			{
				nextProcess = sortedProcessList.get(processIdx++);
			}
			while (nextProcess != null && nextProcess.getArrivalTime() <= quantum) 
			{
				runningProcessList.add(nextProcess);
				
				nextProcess.setTimeToFinish(nextProcess.getExpectedTime());
				
				System.out.println("new process " + nextProcess.getName() + " added");
							
				if(processIdx < sortedProcessList.size() - 1)
				{
					nextProcess = sortedProcessList.get(processIdx++);
				}
				else
				{
					nextProcess = null;
				}
				
				processesAdded = true;
			}
			
			// Sort runningProcessList by Shortest Remaining Time
			if(processesAdded)
			{
				sortProcessesByTimeToFinish(runningProcessList);
			}
			
			// The process to run is the process on top of runningProcessList
			if(runningProcessList.size() > 0)
			{
				currentProcess = runningProcessList.get(0);
				
				// If the process never ran before do some setup...
				if(!currentProcess.getStarted())
				{
					currentProcess.setStarted(true);
					currentProcess.setStartTime(quantum);
					
					System.out.println("process " + currentProcess.getName() + " started");
				}
			}
			
			// Run the current Process if any... 
			if(currentProcess != null)
			{
				timeChart += currentProcess.getName();
				
				// This method, which does nothing, is just to show the current process running for a quantum
				currentProcess.run();
				
				float timeToFinish = currentProcess.getTimeToFinish() - 1;
				if(timeToFinish <= 0)
				{
					timeToFinish = 0;
				}
				
				currentProcess.setTimeToFinish(timeToFinish);
				
				if(timeToFinish == 0)
				{
					System.out.println("process " + currentProcess.getName() + " terminated");
					
					currentProcess.setFinishTime(quantum);
					
					runningProcessList.remove(currentProcess);
					
					processesDone.add(currentProcess);
					
					currentProcess = null;
				}
				else
				{
					sortProcessesByTimeToFinish(runningProcessList);
				}
			}
			else
			{
				System.out.println("no process running (idle)");
			}

			quantum++;	// increment quanta's counter
		}
/*		
		printProcessList(sortedProcessList);
		System.out.println();
*/
	    throughput = processesDone.size();

        oneSimulation += "Simulated order of Shortest Remaining Time\n";
        oneSimulation += timeChart;

        oneSimulation += "\n" + getStringOfAverages(processesDone.size());

        return oneSimulation;//this is the OVERALL STRING REPRESENTATION
	}
	
	private void printProcessList(ArrayList<Process> list)
	{
		System.out.println("Completed processes:");
		for(Process p : list)
		{
			if(p.getFinishTime() == 0)
			{
				continue;
			}
			
			//System.out.printf("%s %d %6.3f %6.3f %2d %2d\n", p.getName(), p.getPriority(), p.getArrivalTime(),
												//p.getExpectedTime(), p.getStartTime(), p.getFinishTime());
		}
		System.out.println("Not completed processes:");
		for(Process p : list)
		{
			if(p.getFinishTime() != 0)
			{
				continue;
			}

//			System.out.printf("%s %d %6.3f %6.3f %2d %2d\n", p.getName(), p.getPriority(), p.getArrivalTime(),
//												p.getExpectedTime(), p.getStartTime(), p.getFinishTime());
		}
	}
	
	private void sortProcessesByTimeToFinish(ArrayList<Process> list)
	{
		Collections.sort(list, new ProcessComparator(2));
	}

	private void sortProcessesByArrivalTime(ArrayList<Process> list)
	{
		Collections.sort(list, new ProcessComparator(1));
	}

	private void sortProcessesByExpectedTime(ArrayList<Process> list)
	{
		Collections.sort(list, new ProcessComparator(0));
	}
	 	
   /**
     * This sets up a string value for all the process objects
     *
     */
	public void introduceProcess() 
	{
        String content = "";
        for (int idx = 0; idx < processList.size(); idx++) 
		{
            content += processList.get(idx).toString();
        }
        // displayProcess(content);	// for testing purposes
        oneSimulation = "\n" + content + "\n"; // adds to simulation's OVERALL STRING REPRESENTATION
        System.out.println(oneSimulation);
    }
	
	/**
     * This returns an array with the statistics information
     *
     * @return averages
     */
    public float[] getStatistics() 
	{
        float[] averages = { averageWaitingTime, averageResponseTime, averageTurnaroundTime, throughput};
        return averages;
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
    public String getStringOfAverages(int numProcesses) 
	{
        String averages = "";
        float totalWaitingTime = 0;
        float totalResponseTime = 0;
        float totalTurnaroundTime = 0;
        float waitingTime = 0;

        //generates the averages for each required statistic
        for (int idx = 0; idx < numProcesses; idx++) 
		{
            waitingTime = processesDone.get(idx).getWaitingTime();
            if (waitingTime < 0) 
			{
                System.out.println("negative!");
            }

            totalWaitingTime += waitingTime;//processesDone.get(i).getWaitingTime();
            totalResponseTime += processesDone.get(idx).getResponseTime();
            totalTurnaroundTime += processesDone.get(idx).getTurnaroundTime();
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
	
	public class ProcessComparator implements Comparator<Process> 
	{
		final private int selector;
		
		ProcessComparator (int selector)
		{
			this.selector = selector;
		}
		
		@Override
		public int compare(Process p1, Process p2) 
		{
			switch(selector)
			{
				case 0:
					return Float.compare(p1.getExpectedTime(), p2.getExpectedTime());
					
				case 1:
					return Float.compare(p1.getArrivalTime(), p2.getArrivalTime());
					
				case 2:
					return Float.compare(p1.getTimeToFinish(), p2.getTimeToFinish());

				default:
					return 0;
			}
		}
	}
}
