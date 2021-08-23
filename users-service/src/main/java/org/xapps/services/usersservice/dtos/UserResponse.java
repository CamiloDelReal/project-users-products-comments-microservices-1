package org.xapps.services.usersservice.dtos;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public UserResponse() {
    }

    public UserResponse(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
