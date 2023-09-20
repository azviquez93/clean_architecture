package cleanArchitectureExample.infrastructure.inputport;

import java.util.List;

import cleanArchitectureExample.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface UserInputPort {

    public User createUser(String firstName, String lastName, String password);

    public User getById(String userId);

    public List<User> getAll();

}