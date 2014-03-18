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
	static int maxPagesOnDisk = 100;
	static int numProcessPages = 10;
	int pagesInMemory;
	private final ArrayDeque<PageFrame> memoryPages;
	private final ArrayList<PageFrame> diskPages;

	public Paging()
	{
		memoryPages = new ArrayDeque<>(maxPagesInMemory);
		diskPages = new ArrayList<>(maxPagesOnDisk);
	}
	
	void runPaging()
	{
		Process process = new Process();
		
		process.runProcess();
	}
	
	private class PageFrame
	{
		public int id;
		public int referencedCount;
		public boolean onDisk;
		public boolean inMemory;
		
		public PageFrame()
		{
		}
	}
		
	private class Process
	{
		private final Random rand;
		int pageReference = 5;
		private ArrayList<PageFrame> processPages;
	
		public Process()
		{
			rand = new Random();
			
			loadProcessOnDisk();
		}
	
		public void runProcess()
		{
			for(int idx = 0; idx < 100; idx++)
			{
				pageReference = generatePageReference();
				
				PageFrame page = processPages.get(pageReference);
				
				page.referencedCount++;
				
				System.out.printf("\nPage %d referenced (%d)\n", page.id, page.referencedCount);
				
				if(!page.inMemory)
				{
					loadPageInMemory(page);
				}
			}
		}
		
		private void loadProcessOnDisk()
		{
			processPages = new ArrayList<>();
			
			for(int idx = 0; idx < numProcessPages; idx++)
			{
				PageFrame page = new PageFrame();
				page.id = idx;
				
				processPages.add(page);
				
				page.onDisk = true;
				page.inMemory = false;
				
				diskPages.add(page);
				
				System.out.printf("Page %d loaded on disk\n", page.id);
			}
		}
		
		private void loadPageInMemory(PageFrame page)
		{
			if(memoryPages.size() == maxPagesInMemory)
			{
				unloadPageFromMemory();
			}
			
			memoryPages.add(page);
			page.inMemory = true;
			page.onDisk = false;
			
			System.out.printf("Page %d loaded in memory\n", page.id);
		}
	
		private void unloadPageFromMemory()
		{
			PageFrame topPage = memoryPages.pop();

			diskPages.set(topPage.id, topPage);
			topPage.onDisk = true;
			topPage.inMemory = false;

			System.out.printf("Page %d unloaded from memory\n", topPage.id);
		}

		private int generatePageReference()
		{
			if((pageReference > 0 && pageReference < (numProcessPages - 1)) && rand.nextInt(10) < 4)
			{
				return pageReference + (rand.nextInt(2) - 1);
			}
			else
			{
				return pageReference = rand.nextInt(numProcessPages);
			}
		}
		
	}

	// For testing only
	public static void main(String[] args) 
	{
		Paging paging = new Paging();
		
		paging.runPaging();
	}
}


