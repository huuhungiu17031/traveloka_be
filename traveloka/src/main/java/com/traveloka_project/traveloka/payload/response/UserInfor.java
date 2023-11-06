package com.traveloka_project.traveloka.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfor {
    private String email;
    private List<String> roles;
}
