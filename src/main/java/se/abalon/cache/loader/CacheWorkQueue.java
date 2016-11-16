package se.abalon.cache.loader;

import java.util.LinkedList;


public class CacheWorkQueue {
	
	private static CacheWorkQueue cacheWorkQueue = null;
	private final int nThreads;
	private final PoolWorker[] threads;
	private final LinkedList<Runnable> queue;
	private int x = 0;
	
	private CacheWorkQueue(int nThreads) {
		this.nThreads = nThreads;
		queue = new LinkedList<Runnable>();
		threads = new PoolWorker[nThreads];

		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}

	public static CacheWorkQueue getCacheWorkQueue(int nThreads){
		if(cacheWorkQueue == null){
			cacheWorkQueue = new CacheWorkQueue(nThreads);
		}
		return cacheWorkQueue;
	}
	
	public void execute(Runnable r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	private class PoolWorker extends Thread {
		public void run() {
			Runnable r;
			while (true) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}

					r = (Runnable) queue.removeFirst();
				}
				// If we don't catch RuntimeException,
				// the pool could leak threads
				try {
					r.run();
				} catch (RuntimeException e) {
					Exception e1 = new Exception("Error while loading cache '" + r.getClass().getName() + "'.",e);
					e1.printStackTrace();
				}
			}
		}
	}
}
