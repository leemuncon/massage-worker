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

/**
 * Created by LiFly on 2015/8/2.
 * Service task used Caller
 * @author jacobow
 * @since 1.0
 */
class Operator4Caller<V> implements Operator {

    private Caller<V> caller;

    private V msg;

    /**
     * create a service caller
     * @param caller the handle caller of message
     * @param msg to handle message
     */
    public Operator4Caller(Caller<V> caller, V msg){
        this.caller = caller;
        this.msg = msg;
    }


    /**
     * the method for task call
     * @param <E>
     * @return
     */
    @Override
    public <E> E run() {
        return caller.call(msg);
    }

    @Override
    public TaskType type() {
        return TaskType.CALL;
    }
}
