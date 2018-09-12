package sample.reactive.rest.business.boundary;

import sample.reactive.rest.business.control.ExecutionInfo;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ExecutionInfo
@Path("/")
public class AsyncApiResource2 {

	private final ExecutorService regExecutors = Executors.newFixedThreadPool(5);

	@GET
	@Path("test1")
	@Produces(MediaType.APPLICATION_JSON)
	public void test1(@Suspended AsyncResponse restResponse) {
		compute()
				.thenApply(r -> restResponse.resume(r))
				.exceptionally(e -> restResponse.resume(e));
	}

	@GET
	@Path("test1b")
	@Produces(MediaType.APPLICATION_JSON)
	public void test1b(@Suspended AsyncResponse restResponse) {
		compute()
				.thenApplyAsync(r -> restResponse.resume(r))
				.exceptionally(e -> restResponse.resume(e));
	}

	private CompletionStage compute() {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "something1";
		}, regExecutors);
	}

	@GET
	@Path("test2")
	@Produces(MediaType.APPLICATION_JSON)
	public String test2() throws InterruptedException {
		Thread.sleep(500);
		return "something2";
	}
}
