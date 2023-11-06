package com.traveloka_project.traveloka.model.embeddable;

import java.io.Serializable;

import com.traveloka_project.traveloka.model.Role;
import com.traveloka_project.traveloka.model.User;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class CompositeKeyUserRole implements Serializable {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
