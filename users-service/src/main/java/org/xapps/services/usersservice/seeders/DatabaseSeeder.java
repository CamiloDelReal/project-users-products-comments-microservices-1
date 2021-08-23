package org.xapps.services.usersservice.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.xapps.services.usersservice.entities.Role;
import org.xapps.services.usersservice.entities.User;
import org.xapps.services.usersservice.repositories.RoleRepository;
import org.xapps.services.usersservice.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;


@Component
public class DatabaseSeeder {

    private BCryptPasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Autowired
    public DatabaseSeeder(BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoles();
        seedUsers();
    }

    private void seedRoles() {
        if(roleRepository.count() == 0) {
            Role role = new Role("Administrator");
            roleRepository.save(role);

            role = new Role("Guest");
            roleRepository.save(role);
        }
    }

    private void seedUsers() {
        if(userRepository.count() == 0) {
            Role admin = roleRepository.findByName("Administrator").orElse(null);
            User user = new User("Root", "Admin", "root@gmail.com", passwordEncoder.encode("root"), List.of(admin));
            userRepository.save(user);
        }
    }

}
