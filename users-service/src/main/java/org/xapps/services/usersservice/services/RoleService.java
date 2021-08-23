package org.xapps.services.usersservice.services;


import org.xapps.services.usersservice.entities.Role;

import java.util.List;


public interface RoleService {

    List<Role> getAll();

    Role getById(Long id);

    Role getByName(String name);

    Role save(Role role);

    void deleteById(Long id);

}
