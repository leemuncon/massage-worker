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

import me.leefly.message.worker.Caller;
import me.leefly.message.worker.Runner;
import me.leefly.message.worker.Worker;

import java.util.concurrent.BlockingQueue;

/**
 * Created by LiFly on 2015/8/4.
 */
public class OperatorCreator {

    /**
     * create process queue worker
     *
     * @param worker the worker to process queue
     * @param queue  the queue to process
     * @param <E>
     * @return
     */
    public static <E> Operator createOperator(Worker<E> worker, BlockingQueue<E> queue){
        return new Operator4Worker<>(worker, queue);
    }

    /**
     * create process message caller
     *
     * @param caller the caller to process message
     * @param msg    the message to process
     * @param <E>
     * @return
     */
    public static <E> Operator createOperator(Caller<E> caller, E msg){
        return new Operator4Caller<>(caller, msg);
    }

    /**
     * create process runner
     * @param runner
     * @param <E>
     * @return
     */
    public static <E> Operator createOperator(Runner runner){
        return new Operator4Runner(runner);
    }

}
