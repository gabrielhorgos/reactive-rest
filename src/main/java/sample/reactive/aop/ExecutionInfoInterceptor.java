package sample.reactive.aop;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.Arrays;

@ExecutionInfo
@Interceptor
public class ExecutionInfoInterceptor {

    @AroundInvoke
    public Object printExecutionInfo(InvocationContext invocationContext) throws Exception {
        String threadName = Thread.currentThread().getName();

        String method = invocationContext.getMethod().toString();

        System.out.println("Thread: " + threadName + " executing " + method);
        //execute intercepted method
        Object returnedVal = invocationContext.proceed();

        System.out.println( "RELEASE : " + threadName + ", execution finished : " + method);

        return returnedVal;
    }

}
