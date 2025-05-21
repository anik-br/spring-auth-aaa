package com.example.authnuzhat.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('READ_USER')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }


    @DeleteMapping("/user")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public String deleteUser() {
        return "User deleted";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN_ACCESS')")
    public String adminAccess() {
        return "Admin Board";
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World";
    }
}
