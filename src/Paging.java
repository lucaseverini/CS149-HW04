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
	
	void runPaging(int runCount, int referenceCount)
	{
		Process process = new Process();
		
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.FIFO, referenceCount);
			printStatistics();
		}
		
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.LRU, referenceCount);
			printStatistics();
		}
		
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.LFU, referenceCount);
			printStatistics();
		}
		
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.MFU, referenceCount);
			printStatistics();
		}
	
		for(int idx = 0; idx < runCount; idx++)
		{
			resetStatistics();	
			process.run(TestType.RANDOM, referenceCount);
			printStatistics();
		}
	}
	
	void resetStatistics()
	{
		referenceCounter = new int[maxPagesOnDisk];
		
		pageFault = 0;
	}
	
	void printStatistics()
	{
		System.out.printf("PageFaults: %d\n", pageFault);
			
		System.out.printf("Page - References\n");
		for(int idx = 0; idx < maxPagesOnDisk; idx++)
		{
			System.out.printf("%-4d   %d\n", idx, referenceCounter[idx]);
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
			
			System.out.printf("\nRunning process with %s paging for %d references\n", testType, referenceCount);
			
			for(int idx = 0; idx < referenceCount; idx++)
			{
				pageReference = generatePageReference();
				
				PageFrame page = processPages.get(pageReference);
				
				page.referencedCount++;
				page.lastReferenceTime = System.currentTimeMillis();
				
				// System.out.printf("\nPage %d referenced (%d)\n", page.id, page.referencedCount);
				
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
			
			// System.out.printf("Page %d loaded in memory\n", page.id);
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

				System.out.printf("Page %d unloaded from memory\n", pageToEvict.id);
			}
			else
			{
				System.out.printf("No Page unloaded from memory\n");
			}
		}

		private int generatePageReference()
		{
			if(rand.nextInt(9) < 7)
			{
				int pageOffset;
				
				do
				{
					pageOffset = (rand.nextInt(2) - 1);
				}
				while((pageReference + pageOffset < 0) || (pageReference + pageOffset > 9));
				
				pageReference += pageOffset;
			}
			else
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

		paging.runPaging(5, 100);
	}
}


