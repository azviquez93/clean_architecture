package cleanArchitectureExample.application;

import cleanArchitectureExample.domain.User;
import cleanArchitectureExample.infrastructure.inputport.UserInputPort;
import cleanArchitectureExample.infrastructure.outputport.EntityRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserUseCase implements UserInputPort {

    final
    EntityRepository entityRepository;

    public UserUseCase(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public User createUser(String firstName, String lastName, String password) {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .firstname(firstName)
                .lastname(lastName)
                .password(password)
                .build();

        return entityRepository.save( user );
    }

    @Override
    public User getById(String userId) {
        return entityRepository.getById( userId, User.class );
    }

    @Override
    public List<User> getAll() {
        return entityRepository.getAll( User.class );
    }

}
