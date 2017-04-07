package test;

import java.util.LinkedList;

public class ThreadsExample implements Runnable {
    
	static int counter = 1; // a global counter
	
	//Counter counter2 = CounterFactory.getCounterInstance();
	
	
	static Object lock = new Object();
	
    void incrementCounter() {
    	synchronized(lock){
    		if(counter<100000){
                System.out.println(Thread.currentThread().getName() + ": " + counter);
                counter++;
        	}
    	}
    }

    @Override
    public void run() {
         while(true){
              incrementCounter();
         }
    }
    
    
    static LinkedList<Thread> threadList = new LinkedList<Thread>();
    
    public static void main(String[] args) {
         ThreadsExample te = new ThreadsExample() ;
         
         Thread monitor = new Thread(){
        	 
        	 Counter counter2 = CounterFactory.getCounterInstance();
        	 @Override
        	    public void run() {
        	         while(true){
        	        		 System.out.println("counter  : " + counter);
        	         }
        	  }
         }; 
         monitor.start();
         
         int threadnumber = 100;
         for(int i = 0 ; i <= threadnumber+1 ; i++)	threadList.add(new Thread(new ThreadsExample()));
         for(Thread thread : threadList) 		    thread.start()  ;   
    }
}


class Counter{
	public int counter = 0;	
}

class CounterFactory {
	static{
		CounterFactory.counter = new Counter();
	}

	static Counter counter = null ;
	
	static  public Counter getCounterInstance(){
		if (counter == null) {
			counter = new Counter(); 
	    }
		return counter ;
	}
}


