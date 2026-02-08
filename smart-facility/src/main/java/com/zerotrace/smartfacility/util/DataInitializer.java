package com.zerotrace.smartfacility.util;

import com.zerotrace.smartfacility.domain.model.Role;
import com.zerotrace.smartfacility.domain.model.Space;
import com.zerotrace.smartfacility.domain.model.User;
import com.zerotrace.smartfacility.domain.repository.SpaceRepository;
import com.zerotrace.smartfacility.domain.repository.UserRepository;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, SpaceRepository spaceRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedSpaces();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            return;
        }
        List<User> users = List.of(
            buildUser("admin@example.com", "Admin User", Role.ADMIN),
            buildUser("manager@example.com", "Manager User", Role.MANAGER),
            buildUser("member@example.com", "Member User", Role.MEMBER)
        );
        userRepository.saveAll(users);
        log.info("Seeded default users (password: ChangeMe123!)");
    }

    private User buildUser(String email, String fullName, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode("ChangeMe123!"));
        user.setRole(role);
        user.setEnabled(true);
        user.setLocked(false);
        return user;
    }

    private void seedSpaces() {
        if (spaceRepository.count() > 0) {
            return;
        }
        List<Space> spaces = List.of(
            new Space("Orion Lab", "Floor 2", 20, Set.of("Projector", "Whiteboard"), true),
            new Space("Nova Room", "Floor 3", 12, Set.of("TV", "Conference Phone"), true),
            new Space("Atlas Hall", "Floor 1", 50, Set.of("Stage", "PA System"), true)
        );
        spaceRepository.saveAll(spaces);
        log.info("Seeded default spaces");
    }
}
