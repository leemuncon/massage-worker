/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Jacobow,https://github.com/leemuncon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.leefly.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by LiFly on 2015/8/2.
 * <p>
 * Queue Pack, can use cache queue
 *
 * @param <V> the queue's type
 * @author jacobow
 * @since 1.0
 */
public class Queues<V> implements Serializable {

    private static Logger log = LoggerFactory.getLogger(Queues.class);

    /**
     * the main queue index
     */
    private final static int ROOT = 0;

    /**
     * default cache queue size
     */
    private final static int CACHE_SIZE = 10000;

    /**
     * current cache index
     */
    private volatile int cacheIndex = ROOT + 1;

    /**
     * cache queue size
     */
    private int cacheSize;

    /**
     * is used cache
     */
    private boolean useCache;

    private ReentrantLock lock;

    /**
     * the main queue
     */
    private BlockingQueue<V> main;

    /**
     * the cache queue collection
     */
    private BlockingQueue<V> cache;

    /**
     * create a queues, default not to create cache queues
     * @param main the main queue
     */
    public Queues(BlockingQueue<V> main) {
        this.main = main;
        this.useCache = false;
    }

    /**
     * create a queues
     * @param main the main queue
     * @param main the cache queue
     */
    public Queues(BlockingQueue<V> main, BlockingQueue<V> cache) {
        this.main = main;
        this.cache = cache;
    }

    /**
     * create a queues
     * @param main the main queue
     * @param cache the cache
     * @param swapper the swapper to swap cache to main
     */
    public Queues(BlockingQueue<V> main, BlockingQueue<V> cache, Swapper<V, V> swapper) {
        this.main = main;
        this.cache = cache;
    }

    /**
     * return the main queue
     *
     * @return
     */
    public BlockingQueue<V> main() {
        return main;
    }

    /**
     * return the cache queue
     *
     * @return
     */
    public BlockingQueue<V> cache() {
        return cache;
    }

    /**
     * return the queues size
     *
     * @return
     */
    public int size() {
        int size = main.size();
        if (cache != null)
            size += cache.size();
        return size;
    }

    /**
     * put the value into main queue, whatever used cache
     *
     * @param value the value into queue
     * @throws InterruptedException
     * @see BlockingQueue#put(Object)
     */
    public void put(V value) throws InterruptedException {
        main.put(value);
    }

    /**
     * take value from main queue
     *
     * @return
     * @throws InterruptedException
     * @see BlockingQueue#take()
     */
    public V take() throws InterruptedException {
        return main.take();
    }

    /**
     * offer the value into main queue, if used cache when main queue was full then offer into the cache
     *
     * @param value the value into queue
     * @return
     * @see BlockingQueue#offer(Object)
     */
    public boolean offer(V value) {
        BlockingQueue<V> queue = queues.get(ROOT);
        boolean done = queue.offer(value);
        if (!done) {
            done = offerCache(value);
        }
        return done;
    }

    /**
     * poll value from main queue, if used cache when main queue was full then poll from the cache
     *
     * @return
     * @see BlockingQueue#poll()
     */
    public V poll() {
        BlockingQueue<V> queue = queues.get(ROOT);
        if (queue.poll() == null)
            return pollCache();
        return queue.poll();
    }

    /**
     * offer the value into main queue, if used cache main queue was full and timeout then offer into the cache
     *
     * @param value   the value into queue
     * @param timeout the wait time
     * @param unit    the unit of time
     * @return
     * @throws InterruptedException
     * @see BlockingQueue#offer(Object, long, TimeUnit)
     */
    public boolean offer(V value, long timeout, TimeUnit unit) throws InterruptedException {
        BlockingQueue<V> queue = queues.get(ROOT);
        return queue.offer(value, timeout, unit);
    }

    /**
     * poll value from main queue, if used cache when the main queue was empty and timeout then poll from cache
     *
     * @param timeout the wait time
     * @param unit    the unit of time
     * @return
     * @throws InterruptedException
     * @see BlockingQueue#poll(long, TimeUnit)
     */
    public V poll(long timeout, TimeUnit unit) throws InterruptedException {
        BlockingQueue<V> queue = queues.get(ROOT);
        V value = queue.poll(timeout, unit);
        if (value == null)
            value = pollCache();
        return value;
    }

    private boolean offerCache(V value) {
        boolean offered = false;
        if (useCache){
            if (queues.get(cacheIndex) == null) {
                lock.lock();
                try {
                    if (queues.get(cacheIndex) == null)
                        queues.add(cacheIndex, new LinkedBlockingQueue<>(cacheSize));
                    if (!(offered = queues.get(cacheIndex).offer(value))) {
                        cacheIndex++;
                        return offerCache(value);
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                offered = queues.get(cacheIndex).offer(value);
                if (!offered)
                    return offerCache(value);
            }
        }
        return offered;
    }

    private V pollCache() {
        if (useCache) {
            List<BlockingQueue<V>> cache = queues.subList(ROOT + 1, queues.size());// the cache
            Iterator<BlockingQueue<V>> it = cache.iterator();
            while (it.hasNext()) {
                BlockingQueue<V> next = it.next();
                V value = next.poll();
                if (value != null)
                    return value;
            }
        }
        return null;
    }

}
