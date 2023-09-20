package cleanArchitectureExample.infrastructure.outputport;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EntityRepository {

    public <T> T save( T reg );

    public <T> T getById( String id, Class<T> clazz );

    public <T> List<T> getAll( Class<T> clazz );

}
