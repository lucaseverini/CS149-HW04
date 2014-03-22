/*
*	Paging.java
*
*   Assignment #4 - CS149 - SJSU
*
*	By Luca Severini (lucaseverini@mac.com)
* 
*	San Jose Mar-17-2014
*/

import java.util.*;

/**
 * This class implements the Paging algorithms
 */
public class Paging 
{
	static int maxPagesInMemory = 4;
	static int maxPagesOnDisk = 10;
	static int numProcessPages = 10;
	private final ArrayDeque<PageFrame> memoryPages;
	private final ArrayList<PageFrame> diskPages;
	private int[] referenceCounter;
	private int pageFault;
	private String output;
	
	public enum TestType 
	{  
		FIFO,	// First-In First-Out
		LRU,	// Least Recently Used
		LFU,	// Least Frequently Used
		MFU,	// Most Frequently Used
		RANDOM	// Random Pick
	}

	public Paging()
	{
		memoryPages = new ArrayDeque<>(maxPagesInMemory);
		diskPages = new ArrayList<>(maxPagesOnDisk);
	}
	
	String runPaging(int runCount, int referenceCount)
	{
		Process process = new Process();
		int maxPageFault = 0, minPageFault = Integer.MAX_VALUE;
		String worstRun = "", bestRun = "";
		
		output = "";
		
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.FIFO, referenceCount);
			printStatistics();
			
			if(pageFault > maxPageFault)
			{
				maxPageFault = pageFault;
				worstRun = TestType.FIFO + " #" + (idx +1);
			}
			if(pageFault < minPageFault)
			{
				minPageFault = pageFault;
				bestRun = TestType.FIFO + " #" + (idx +1);
			}
		}
		
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.LRU, referenceCount);
			printStatistics();

			if(pageFault > maxPageFault)
			{
				maxPageFault = pageFault;
				worstRun = TestType.LRU + " #" + (idx +1);
			}
			if(pageFault < minPageFault)
			{
				minPageFault = pageFault;
				bestRun = TestType.LRU + " #" + (idx +1);
			}
		}
		
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.LFU, referenceCount);
			printStatistics();
			
			if(pageFault > maxPageFault)
			{
				maxPageFault = pageFault;
				worstRun = TestType.LFU + " #" + (idx +1);
			}
			if(pageFault < minPageFault)
			{
				minPageFault = pageFault;
				bestRun = TestType.LFU + " #" + (idx +1);
			}
		}
		
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.MFU, referenceCount);
			printStatistics();
			
 			if(pageFault > maxPageFault)
			{
				maxPageFault = pageFault;
				worstRun = TestType.MFU + " #" + (idx +1);
			}
			if(pageFault < minPageFault)
			{
				minPageFault = pageFault;
				bestRun = TestType.MFU + " #" + (idx +1);
			}
		}
	
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.RANDOM, referenceCount);
			printStatistics();
			
			if(pageFault > maxPageFault)
			{
				maxPageFault = pageFault;
				worstRun = TestType.RANDOM + " #" + (idx +1);
			}
			if(pageFault < minPageFault)
			{
				minPageFault = pageFault;
				bestRun = TestType.RANDOM + " #" + (idx +1);
			}
		}
		
		output += String.format("\nBest run: %s with %d page faults\n", bestRun, minPageFault);
		output += String.format("Worst run: %s with %d page faults\n", worstRun, maxPageFault);
		
		return output;
	}
	
	void resetStatistics()
	{
		referenceCounter = new int[maxPagesOnDisk];
		
		pageFault = 0;
	}
	
	void printStatistics()
	{
		output += String.format("\n== Statistics ==\n");
		output += String.format("PageFaults: %d\n", pageFault);
			
		output += String.format("Page - # of References\n");
		for(int idx = 0; idx < maxPagesOnDisk; idx++)
		{
			output += String.format("%-4d   %d\n", idx, referenceCounter[idx]);
		}
	}
	
	private class PageFrame
	{
		public int id;
		public int referencedCount;
		public long lastReferenceTime;
		public boolean onDisk;
		public boolean inMemory;
		
		public PageFrame()
		{
		}
	}
		
	private class Process
	{
		private final Random rand;
		int pageReference = 0;
		private ArrayList<PageFrame> processPages;
		private TestType test;
	
		public Process()
		{
			rand = new Random();
			
			initPageTable();
		}
	
		public void run(TestType testType, int referenceCount)
		{
			test = testType;
			
			output += String.format("\nRunning process with %s paging for %d references\n", testType, referenceCount);
			
			for(int idx = 0; idx < referenceCount; idx++)
			{
				pageReference = generatePageReference();
				
				PageFrame page = processPages.get(pageReference);
				
				page.referencedCount++;
				page.lastReferenceTime = System.currentTimeMillis();
				
				output += String.format("\nPage %d referenced (%d)\n", page.id, page.referencedCount);
				
				if(!page.inMemory)
				{
					pageFault++;
					
					loadPageToMemory(page);
				}
			}
		}
		
		private void initPageTable()
		{
			processPages = new ArrayList<>();
			
			for(int idx = 0; idx < numProcessPages; idx++)
			{
				PageFrame page = new PageFrame();
				page.id = idx;
				
				processPages.add(page);
				
				page.onDisk = true;			// at beginning every page is on disk...
				page.inMemory = false;
				
				diskPages.add(page);
				
				// System.out.printf("Page %d loaded on disk\n", page.id);
			}
		}
		
		private void loadPageToMemory(PageFrame page)
		{
			if(memoryPages.size() == maxPagesInMemory)
			{
				unloadPageFromMemory();
			}
			
			memoryPages.add(page);
			page.inMemory = true;
			page.onDisk = false;
			
			output += String.format("Page %d loaded in memory\n", page.id);
		}
	
		private void unloadPageFromMemory()
		{
			PageFrame pageToEvict = null;
			
			switch(test)
			{
				case FIFO:
					pageToEvict = memoryPages.pop();
					break;
					
				case LRU:
					long oldestTime = 0;
					for(PageFrame p : memoryPages)
					{
						if(p.lastReferenceTime > oldestTime)
						{
							oldestTime = p.lastReferenceTime;
							pageToEvict = p;
						}
					}
					memoryPages.remove(pageToEvict);
					break;

				case LFU:
					int smallestCount = Integer.MAX_VALUE;
					for(PageFrame p : memoryPages)
					{
						if(p.referencedCount < smallestCount)
						{
							smallestCount = p.referencedCount;
							pageToEvict = p;
						}
					}
					memoryPages.remove(pageToEvict);
					break;
					
				case MFU:
					int largestCount = 0;
					for(PageFrame p : memoryPages)
					{
						if(p.referencedCount > largestCount)
						{
							largestCount = p.referencedCount;
							pageToEvict = p;
						}
					}
					memoryPages.remove(pageToEvict);
					break;
					
				case RANDOM:
					Iterator<PageFrame> iter = memoryPages.iterator();
					for(int idx = rand.nextInt(memoryPages.size()); idx >= 0; idx--)
					{
						pageToEvict = iter.next();
					}
					memoryPages.remove(pageToEvict);
					break;				
			}
			
			if(pageToEvict != null)
			{
				diskPages.set(pageToEvict.id, pageToEvict);
				pageToEvict.onDisk = true;
				pageToEvict.inMemory = false;

				output += String.format("Page %d unloaded from memory\n", pageToEvict.id);
			}
			else
			{
				output += String.format("No Page unloaded from memory\n");
			}
		}

		private int generatePageReference()
		{
			if(rand.nextInt(9) < 7)  // generate a random ∆i to be -1, 0 or +1
			{
				int pageOffset;
				
				do
				{
					pageOffset = (rand.nextInt(2) - 1);
				}
				while((pageReference + pageOffset < 0) || (pageReference + pageOffset > 9));
				
				pageReference += pageOffset;
			}
			else					// randomly generate 2 ≤ ∆i ≤ 8
			{
				pageReference = rand.nextInt(numProcessPages);
			}
	
			referenceCounter[pageReference] = referenceCounter[pageReference] + 1;

			return pageReference;
		}	
	}

	// For testing only
	public static void main(String[] args) 
	{
		Paging paging = new Paging();

		String out = paging.runPaging(5, 100);
		System.out.println(out);
	}
}


