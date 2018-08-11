package sample.reactive.rest.business.boundary;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class CommonExecService {

    private ExecutorService regExecutors = Executors.newFixedThreadPool(5);

    public ExecutorService getExecService() {
        if (this.regExecutors == null) {
            this.regExecutors =  Executors.newFixedThreadPool(5);
        }

        return this.regExecutors;
    }

}
