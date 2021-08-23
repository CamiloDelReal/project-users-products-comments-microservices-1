package org.xapps.services.usersservice.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.xapps.services.usersservice.entities.Role;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
