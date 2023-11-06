package com.traveloka_project.traveloka.payload.resquest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String password;
}
