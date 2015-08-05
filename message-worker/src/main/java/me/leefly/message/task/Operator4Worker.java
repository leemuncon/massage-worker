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

package me.leefly.message.task;

import me.leefly.message.worker.Worker;

import java.util.concurrent.BlockingQueue;

/**
 * Created by LiFly on 2015/8/2.
 * Service task worker
 * @author jacobow
 * @since 1.0
 */
class Operator4Worker<E> implements Operator {

    private Worker<E> worker;

    private BlockingQueue<E> queue;

    /**
     * create a service worker
     * @param worker the handle worker for put value or take value
     * @param queue the queue for take or put
     */
    public Operator4Worker(Worker<E> worker, BlockingQueue<E> queue){
        this.worker = worker;
        this.queue = queue;
    }


    @Override
    public <E> E run() {
        worker.work(queue);
        return null;
    }

    @Override
    public TaskType type() {
        return TaskType.RUN;
    }
}
