package com.meetingBuddy.meetingBuddy.controller;

import com.meetingBuddy.meetingBuddy.entity.User;
import com.meetingBuddy.meetingBuddy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class UiController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboardUi(Model model, Authentication authentication, HttpServletRequest request) {
        String username = authentication.getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();
            return "redirect:/login?error=invalid_user";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "dashboard";
    }
}
