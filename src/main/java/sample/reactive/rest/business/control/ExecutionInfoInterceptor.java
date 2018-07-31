package sample.reactive.rest.business.control;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

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
