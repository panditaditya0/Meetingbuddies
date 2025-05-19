package com.meetingBuddy.meetingBuddy.controller;

import com.meetingBuddy.meetingBuddy.entity.User;
import com.meetingBuddy.meetingBuddy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("userDto", new User());
        model.addAttribute("users", userRepository.findAll());
        return "user-management";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("userDto") User userDto) {
        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRole(userDto.getRole());
        userRepository.save(newUser);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> userToDelete = userRepository.findById(id);

        if (userToDelete.isPresent()) {
            if (userToDelete.get().getUsername().equals(currentUsername)) {
                request.getSession().invalidate();
                SecurityContextHolder.clearContext();
                userRepository.deleteById(id);

                return "redirect:/login";
            }
        }
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

}
