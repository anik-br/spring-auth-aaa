package com.example.authnuzhat.init;

import com.example.authnuzhat.models.Privilege;
import com.example.authnuzhat.models.Role;
import com.example.authnuzhat.repository.PrivilegeRepository;
import com.example.authnuzhat.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        createPrivileges();
        createRoles();
        //createUsers();
    }

//    private void createPrivileges() {
//        createPrivilegeIfNotFound("CREATE_USER");
//        createPrivilegeIfNotFound("READ_USER");
//        createPrivilegeIfNotFound("UPDATE_USER");
//        createPrivilegeIfNotFound("DELETE_USER");
//        createPrivilegeIfNotFound("ADMIN_ACCESS");
//        createPrivilegeIfNotFound("CREATE_ADMIN_BY_SUPER_ADMIN_ONLY");
//    }

    private void createPrivileges() {
        createPrivilegeIfNotFound("CREATE_USER");
        createPrivilegeIfNotFound("READ_USER");
        createPrivilegeIfNotFound("UPDATE_USER");
        createPrivilegeIfNotFound("DELETE_USER");
        createPrivilegeIfNotFound("ADMIN_ACCESS");
        createPrivilegeIfNotFound("CREATE_ADMIN_BY_SUPER_ADMIN_ONLY");
        createPrivilegeIfNotFound("TEACHER_ACCESS");
        createPrivilegeIfNotFound("SUPER_ADMIN_ACCESS");
        createPrivilegeIfNotFound("TEACHER_CREATE_QUESTION");
        createPrivilegeIfNotFound("TEACHER_READ_QUESTION");
        createPrivilegeIfNotFound("TEACHER_UPDATE_QUESTION");
        createPrivilegeIfNotFound("TEACHER_DELETE_QUESTION");
        createPrivilegeIfNotFound("TEACHER_ACCESS");
    }

    private void createRoles() {
        Role superAdminRole= createRoleIfNotFound("SUPER_ADMIN");
        superAdminRole.setPrivileges(Set.of(
                findPrivilege("CREATE_USER"),
                findPrivilege("READ_USER"),
                findPrivilege("UPDATE_USER"),
                findPrivilege("DELETE_USER"),
                findPrivilege("ADMIN_ACCESS"),
                findPrivilege("TEACHER_ACCESS"),
                findPrivilege("CREATE_ADMIN_BY_SUPER_ADMIN_ONLY")
        ));

        roleRepository.save(superAdminRole);

        Role teacherRole = createRoleIfNotFound("TEACHER");
        teacherRole.setPrivileges(Set.of(
                findPrivilege("TEACHER_CREATE_QUESTION"),
                findPrivilege("TEACHER_READ_QUESTION"),
                findPrivilege("TEACHER_UPDATE_QUESTION"),
                findPrivilege("TEACHER_DELETE_QUESTION"),
                findPrivilege("TEACHER_ACCESS")

        ));
        roleRepository.save(teacherRole);

        Role adminRole = createRoleIfNotFound("ADMIN");
        adminRole.setPrivileges(Set.of(
                findPrivilege("CREATE_USER"),
                findPrivilege("READ_USER"),
                findPrivilege("UPDATE_USER"),
                findPrivilege("DELETE_USER"),
                findPrivilege("ADMIN_ACCESS")
        ));
        roleRepository.save(adminRole);

        Role userRole = createRoleIfNotFound("USER");
        userRole.setPrivileges(Set.of(findPrivilege("READ_USER")));
        roleRepository.save(userRole);

        Role userModerator = createRoleIfNotFound("MODERATOR");
        userModerator.setPrivileges(Set.of(findPrivilege("UPDATE_USER"), findPrivilege("READ_USER")));
        roleRepository.save(userModerator);

    }



    // ... helper methods
    private Privilege findPrivilege(String name){
        return privilegeRepository.findByName(name).orElse(null);
    }

    private void createPrivilegeIfNotFound(String privilegeName) {
        Optional<Privilege> existingPrivilege = privilegeRepository.findByName(privilegeName);

        if (existingPrivilege.isEmpty()) {
            Privilege newPrivilege = new Privilege();
            newPrivilege.setName(privilegeName);
            privilegeRepository.save(newPrivilege);
            log.info("Created privilege: " + privilegeName);
        } else {
            log.debug("Privilege already exists: " + privilegeName);
        }
    }

    private Role createRoleIfNotFound(String roleName) {
        Optional<Role> existingRole = roleRepository.findByName(roleName);

        if (existingRole.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(roleName);
            roleRepository.save(newRole);
            log.info("Created role: " + roleName);
            return newRole;
        } else {
            log.debug("Role already exists: " + roleName);
            return existingRole.get();
        }

    }

}
