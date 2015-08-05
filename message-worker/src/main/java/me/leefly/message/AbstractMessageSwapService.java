package me.leefly.message;

import me.leefly.message.task.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2015/7/30.
 * <p/>
 * 消息转换service
 *
 * @author lifei
 * @version 1.0
 */
public abstract class AbstractMessageSwapService implements MessageSwapService {

    protected static Logger logger = LoggerFactory.getLogger(AbstractMessageSwapService.class);

    protected ExecutorService executor;

    protected Future<?> future;

    protected Operator runner;

    public AbstractMessageSwapService(ExecutorService executor){
        this.executor = executor;
    }

//    @Override
    public void init(Operator runner) {
        this.runner = runner;
    }

    @Override
    public boolean alive() {
        if (future == null || future.isDone() || future.isCancelled())
            return false;
        return true;
    }

    @Override
    public void destroy() {
        future.cancel(false);
    }
}
