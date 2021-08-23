package org.xapps.services.usersservice.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Setter
@Getter
public class UserRequest {
    @NotNull(message = "First name cannot be empty")
    @Size(min = 3, message = "First must not be less than 3 characters")
    private String firstName;

    private String lastName;

    @Email
    @NotNull(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Password cannot be empty")
    private String password;

    public UserRequest() {
    }

    public UserRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
