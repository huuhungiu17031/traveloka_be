package com.traveloka_project.traveloka.payload.resquest;

import com.traveloka_project.traveloka.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    public User dto() {
        User newUser = new User();
        newUser.setEmail(this.email);
        newUser.setPassword(this.password);
        newUser.setFirstName(this.firstName);
        newUser.setLastName(this.lastName);
        return newUser;
    }

    public CreateUserRequest otd(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        return this;
    }
}
