package sample.reactive.rest.persistence;

import sample.reactive.aop.ExecutionInfo;
import sample.reactive.rest.model.UserRegistration;
import sample.reactive.rest.model.User;
import sample.reactive.rest.exception.DuplicateUsernameException;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@ExecutionInfo
@Singleton
public class ApplicationStorage {

    static final String APPLICATIONS_KEY = "applicationStorage";
    static final String USERS_KEY = "userStorage";

    private AtomicLong applicationPrimaryKey = new AtomicLong();
    private AtomicLong userPrimaryKey = new AtomicLong();

    private ReentrantLock userLock = new ReentrantLock();
    private ReentrantLock regApplicationLock = new ReentrantLock();


    private ConcurrentHashMap<String, Object> storage = new ConcurrentHashMap<>();

    private HashMap<Long, User> getUsers() {
        HashMap<Long, User> users = (HashMap<Long, User>) storage.get(USERS_KEY);

        if (users == null) {
            users = new HashMap<Long, User>();
            storage.put(USERS_KEY, users);
        }

        return users;
    }

    private List<UserRegistration> getApplications() {

        List<UserRegistration> applications = (List<UserRegistration>) storage.get(APPLICATIONS_KEY);

        if (applications == null) {
            applications = Collections.synchronizedList(new ArrayList<>());
            storage.put(APPLICATIONS_KEY, applications);
        }

        return applications;
    }

    public UserRegistration saveApplication(UserRegistration userRegistration) throws DuplicateUsernameException {
        regApplicationLock.lock();

        try {
            String username = userRegistration.getUserData().getUsername();
            boolean alreadyExists = getApplications()
                    .stream()
                    .anyMatch(a -> a.getUserData().getUsername().equals(username));

            if (alreadyExists) {
                throw new DuplicateUsernameException("There already exists a registration application for username : " +
                        username);
            }

            userRegistration.setId(applicationPrimaryKey.getAndIncrement());
            getApplications().add(userRegistration);
        } finally {
            regApplicationLock.unlock();
        }

        return userRegistration;
    }

    public User storeUser(User user) throws DuplicateUsernameException {
        userLock.lock();
        try {
            boolean alreadyExists = getUsers().entrySet()
                    .stream()
                    .anyMatch(entry -> entry.getValue().getUsername().equals(user.getUsername()));

            if (alreadyExists) {
                throw new DuplicateUsernameException("There already exists an user with username : " + user.getUsername());
            }

            user.setId(userPrimaryKey.getAndIncrement());

            getUsers().put(user.getId(), user);
        } finally {
            userLock.unlock();
        }

        return user;
    }

    public void removeApplicationsForUsername(String username) {
        getApplications().removeIf(aplication -> aplication.getUserData().getUsername().equals(username));
    }
}
