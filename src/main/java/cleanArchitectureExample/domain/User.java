package cleanArchitectureExample.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String password;
}
