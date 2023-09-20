package cleanArchitectureExample.infrastructure.inputadapter.http;

import java.util.List;

import cleanArchitectureExample.domain.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cleanArchitectureExample.infrastructure.inputport.UserInputPort;

@RestController
@RequestMapping(value = "user")
public class UserAPI {

    final UserInputPort userInputPort;

    public UserAPI(UserInputPort userInputPort) {
        this.userInputPort = userInputPort;
    }

    @PostMapping(value = "create", produces=MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String password ) {
        return userInputPort.createUser(firstName, lastName, password);
    }

    @PostMapping(value = "get", produces=MediaType.APPLICATION_JSON_VALUE)
    public User get( @RequestParam String userId ) {
        return userInputPort.getById(userId);
    }

    @PostMapping(value = "getall", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return userInputPort.getAll();
    }

}
