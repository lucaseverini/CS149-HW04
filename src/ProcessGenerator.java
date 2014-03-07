
import java.util.*;

/**
 * This class generates a fixed number of processes that will be dictated by the
 * class that initializes it. The method that generates these processes will
 * return an array list of Process objects.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ProcessGenerator {

    // instance variables - replace the example below with your own
    private int numProcesses;//Number of Process objects in array
    private ArrayList<Process> processArrayList;
    private ArrayList<Process> unsortedArrayList;
    private Random generator;
    private Process newProcess;
    private int RandomSeed;

    /**
     * Constructor: objects of ProcessGenerator numProcesses is passed in as the
     * number of processes needed to be generated
     *
     * @param numProcesses: an integer, number of processes to be created
     * @param RandomSeed: an integer, the random seed value used to generate the arrival, expected and priority of processes
     */
    public ProcessGenerator(int numProcesses, int RandomSeed) {
        // initialise instance variables
        this.numProcesses = numProcesses;
        processArrayList = new ArrayList<>();
        this.RandomSeed = RandomSeed;

    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @return an arraylist Process objects sorted by arrival time
     */
    public ArrayList<Process> generateProcesses() {
        int letterCounter = 65;
        int firstNumber = 1;
        float arrivalTime;
        int expectedTime;
        int priority;
        

        generator = new Random(RandomSeed); //paramater is seed used for random number generator

        for (int i = 0; i < numProcesses; i++) {
            String name = "";
            //create random number for arrival time
            arrivalTime = (float) (generator.nextDouble() * 59.0);
            //create random number for expected run time
            expectedTime = generator.nextInt(5) + 1;

            //create new Process object and pass in these four

            if (letterCounter < 91) {
                name += firstNumber + "-" + (char) (letterCounter);
                letterCounter++;
            } else {
                letterCounter = 65;
                firstNumber++;
                name += firstNumber + "-" + (char) (letterCounter);
                letterCounter++;
            }
            

            newProcess = new Process(arrivalTime, expectedTime, name);
            //put the Process object in array processArray using i as index
            processArrayList.add(newProcess);

            //System.out.print(newProcess.toString());
            /*System.out.println(processArrayList.get(i).getArrivalTime()
             + "  " + processArrayList.get(i).getExpectedTime()
             + "  " + processArrayList.get(i).getPriority());
             */
        }
        //System.out.println("\n  ");

        unsortedArrayList = new ArrayList<>();
        unsortedArrayList = (ArrayList<Process>)processArrayList.clone();
        
        Collections.sort(processArrayList, new Comparator<Process>() {
            @Override
            public int compare(Process process1, Process process2) {
                if (process1.getArrivalTime() < process2.getArrivalTime()) {
                    return -1;
                }
                if (process1.getArrivalTime() > process2.getArrivalTime()) {
                    return +1;
                }
                return 0;
            }
        });

        /* for(int i = 0; i < numProcesses; i++){
            
         System.out.println("arrival time:  " + processArrayList.get(i).getArrivalTime()
         + "  " + processArrayList.get(i).getExpectedTime()
         + "  " + processArrayList.get(i).getPriority());
         }*/
        return processArrayList;
    }
    
    public ArrayList<Process> getUnsortedArrayList(){
        return unsortedArrayList;
    } 
    
    
}
