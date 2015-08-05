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

package me.leefly.message.executor;

import me.leefly.message.task.Operator;
import me.leefly.message.task.OperatorCreator;
import me.leefly.message.task.TaskCreator;
import me.leefly.message.task.TaskType;
import me.leefly.message.worker.Caller;
import me.leefly.message.worker.Runner;
import me.leefly.message.worker.Worker;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * Created by LiFly on 2015/8/3.
 */
public class MessageExecutor<T> {

    private ExecutorService service;

    private Map<Operator, ExecutorState<T>> result;

    public MessageExecutor(ExecutorService service) {
        result = new HashMap<>();
        this.service = service;
    }

    public void setOperator(Worker<T> worker, BlockingQueue<T> queue) {
        result.put(OperatorCreator.createOperator(worker, queue), null);
    }

    public void setOperator(Caller<T> caller, T msg) {
        result.put(OperatorCreator.createOperator(caller, msg), null);
    }

    public void setOperator(Runner runner) {
        result.put(OperatorCreator.createOperator(runner), null);
    }

    public void execute() {
        Iterator<Operator> iterator = result.keySet().iterator();
        while (iterator.hasNext()) {
            Operator op = iterator.next();
            ExecutorState<T> state = result.get(op);
            if (state == null) {
                if (TaskType.RUN.equals(op.type()))
                    state = new ExecutorState<>(service.submit(TaskCreator.createTask4Run(op)));
                if (TaskType.CALL.equals(op.type()))
                    state = new ExecutorState<>(service.submit(TaskCreator.createTask4Call(op)));
                result.put(op, state);
            } else if (!state.alive()) {
                result.put(op, null);
            }
        }
    }
}
