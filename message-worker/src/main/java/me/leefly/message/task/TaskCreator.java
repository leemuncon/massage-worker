/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 Jacobow,https://github.com/leemuncon
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

import java.util.concurrent.Callable;

/**
 * Created by LiFly on 2024/8/13.
 */
public class TaskCreator {

    /**
     * create operator task for runs
     *
     * @param operator the worker to process queue
     * @return
     */
    public static Runnable createTask4Run(Operator operator) {
        return new Task4Run(operator);
    }

    /**
     * create operator task for call
     *
     * @param operator the worker to process queue
     * @param <E>
     * @return
     */
    public static <E> Callable<E> createTask4Call(Operator operator) {
        return new Task4Call<>(operator);
    }

}
