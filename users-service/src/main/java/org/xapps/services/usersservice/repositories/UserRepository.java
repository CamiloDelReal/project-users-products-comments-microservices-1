package org.xapps.services.usersservice.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.xapps.services.usersservice.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
