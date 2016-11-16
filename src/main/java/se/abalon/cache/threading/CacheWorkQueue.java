package se.abalon.cache.threading;

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

    public static CacheWorkQueue getCacheeWorkQueue(int nThreads){
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
                            //System.out.println("Waiting...");
                        } catch (InterruptedException ignored) {
                        }
                    }

                    r = (Runnable) queue.removeFirst();
//					System.out.println("Removed first..." + r);
                }

                // If we don't catch RuntimeException,
                // the pool could leak threads
                try {
//					System.out.println("Run starting..." + r);
                    r.run();
//					System.out.println("Run complete..." + r);
                } catch (RuntimeException e) {
                    System.out.println("PoolWorker error!!!");
                    // You might want to log something here
                }
            }
        }
    }
}
