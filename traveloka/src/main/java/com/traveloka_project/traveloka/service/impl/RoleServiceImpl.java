package com.traveloka_project.traveloka.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traveloka_project.traveloka.exception.NotFoundException;
import com.traveloka_project.traveloka.model.Role;
import com.traveloka_project.traveloka.repository.RoleRespository;
import com.traveloka_project.traveloka.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRespository roleRespository;

    @Override
    public Role findByRole(String role) {
        Optional<Role> optional = roleRespository.findByRole(role);
        if (optional.isEmpty())
            throw new NotFoundException("Role not Found");
        return optional.get();
    }

}
