package com.example.authnuzhat.services;

import com.example.authnuzhat.models.Role;
import com.example.authnuzhat.models.User;
import com.example.authnuzhat.payload.request.SignupRequest;
import com.example.authnuzhat.payload.response.MessageResponse;
import com.example.authnuzhat.repository.RoleRepository;
import com.example.authnuzhat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );

        Set<Role> roles = getRoles(signUpRequest.getRole());
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private Set<Role> getRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            roles.add(userRole);
        } else {
            for (String role : strRoles) {
                switch (role.toLowerCase()) {
                    case "admin":
                        roles.add(roleRepository.findByName("ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Role not found.")));
                        break;
                    case "mod":
                        roles.add(roleRepository.findByName("MODERATOR")
                                .orElseThrow(() -> new RuntimeException("Error: Role not found.")));
                        break;
                    default:
                        roles.add(roleRepository.findByName("USER")
                                .orElseThrow(() -> new RuntimeException("Error: Role not found.")));
                }
            }
        }
        return roles;
    }
}
