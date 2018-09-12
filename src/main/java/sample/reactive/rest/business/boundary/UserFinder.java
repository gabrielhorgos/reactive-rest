package sample.reactive.rest.business.boundary;

import sample.reactive.rest.business.boundary.exception.DuplicateUsernameException;

import javax.inject.Inject;
import java.util.function.Supplier;

public class UserFinder implements Supplier {

	@Inject
	private ApplicationStorage applicationStorage;

	@Override public String get() {
		return "Alma";
	}
}
