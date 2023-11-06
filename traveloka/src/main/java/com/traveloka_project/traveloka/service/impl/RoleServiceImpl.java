package com.traveloka_project.traveloka.service.impl;

import java.util.Optional;

import com.traveloka_project.traveloka.util.ErrorMessage;
import org.springframework.stereotype.Service;

import com.traveloka_project.traveloka.exception.NotFoundException;
import com.traveloka_project.traveloka.model.Role;
import com.traveloka_project.traveloka.repository.RoleRespository;
import com.traveloka_project.traveloka.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRespository roleRespository;
    
    public RoleServiceImpl(RoleRespository roleRespository) {
        this.roleRespository = roleRespository;
    }
    @Override
    public Role findByRole(String role) {
        Optional<Role> optional = roleRespository.findByRole(role);
        if (optional.isEmpty())
            throw new NotFoundException(ErrorMessage.generateNotFoundMessage("Role") + role);
        return optional.get();
    }

}
