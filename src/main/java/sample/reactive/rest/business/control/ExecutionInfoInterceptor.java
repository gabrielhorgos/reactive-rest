package sample.reactive.rest.business.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@ExecutionInfo
@Interceptor
public class ExecutionInfoInterceptor {

    @AroundInvoke
    public Object printExecutionInfo(InvocationContext invocationContext) throws Exception {
        Logger logger = LoggerFactory.getLogger(invocationContext.getClass());

        String threadName = Thread.currentThread().getName();

        String method = invocationContext.getMethod().toString();

        logger.debug("AQUIRE  T : " + threadName + " executing " + method);
        //execute intercepted method
        Object returnedVal = invocationContext.proceed();

        logger.debug("RELEASE T : " + threadName + ", execution finished : " + method);

        return returnedVal;
    }

}
