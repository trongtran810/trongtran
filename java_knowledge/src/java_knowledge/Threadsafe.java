/**
 * Reference: https://www.baeldung.com/java-thread-safety
 */
package java_knowledge;

import java.lang.Thread.State;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 
 */
public class Threadsafe {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
		concurrentMap.put("Key 0", 0);
		List<Thread> threads = new LinkedList<Thread>();
		threads.add(new MultithreadingExThread(concurrentMap));
		threads.add(new MultithreadingExThread(concurrentMap));
		threads.add(new MultithreadingExThread(concurrentMap));
//		threads.add(new Thread(()->concurrentMap.put("1", "one")));
//		threads.add(new Thread(()->concurrentMap.put("2", "two")));
//		threads.add(new Thread(()->concurrentMap.put("3", "three")));
//		ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(3);
		
//		for (int i = 0; i < 3; i++) {
//			Thread curThread = threads.get(i);
////			curThread.start();
//		    WORKER_THREAD_POOL.submit(() -> {
//		        try {
//		        	curThread.start();
//		        } catch (Exception e) {
//		            Thread.currentThread().interrupt();
//		        }
//		    });
//		}

		// wait for the latch to be decremented by the two remaining threads
		// Waiting for both threads to finish
        try {
            threads.get(0).join();
            threads.get(1).join();
            threads.get(2).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		System.out.println(concurrentMap);
	}

}

class MultithreadingExThread extends Thread {
	private Map<String,Integer> ccm;
	
	public void run()
	{
		try {
			for (int i = 0; i < 10; i++) {
            	ccm.put("Key" + i, i);
            }
			
//			ccm.put(String.valueOf(this.order), getName());
			// Displaying the thread that is running
			System.out.println(
				".Thread " + Thread.currentThread().getId()
				+ " is running");
			System.out.println(
					".Thread " + Thread.currentThread().getId()
					+ " is done");
		}
		catch (Exception e) {
			// Throwing an exception
			System.out.println("Exception is caught");
		}
		
	}
	
	public MultithreadingExThread(Map<String,Integer> ccm) {
		this.ccm = ccm;
	}
}

// The method neither relies on external state nor maintains state at all
class MathUtilsStateless {
    
    public static BigInteger factorial(int number) {
        BigInteger f = new BigInteger("1");
        for (int i = 2; i <= number; i++) {
            f = f.multiply(BigInteger.valueOf(i));
        }
        return f;
    }
}

// Moreover, if MessageService were actually mutable, but multiple threads only have read-only access to it, it’s thread-safe as well.
class MessageServiceImmutable {
    
    private final String message;

    public MessageServiceImmutable(String message) {
        this.message = message;
    }

	public String getMessage() {
		return message;
	}
}

class ReentrantReadWriteLockCounter {
    
    private int counter;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReadLock readLock = rwLock.readLock();
    private final WriteLock writeLock = rwLock.writeLock();
    
    public void incrementCounter() {
        writeLock.lock();
        try {
            counter += 1;
        } finally {
            writeLock.unlock();
        }
    }
    
    public int getCounter() {
        readLock.lock();
        try {
            return counter;
        } finally {
            readLock.unlock();
        }
    }

}
