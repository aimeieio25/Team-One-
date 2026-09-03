package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ProfileController {

        @Autowired
        UserRepository userRepository;

        @GetMapping("/profile")
        public String profile(Model model) {
                DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

                String email = principal.getEmail();

                User user = userRepository
                                .findUserByUsername(email)
                                .orElseThrow();

                model.addAttribute("user", user);

                return "profile/profile";
        }

        @GetMapping("/profile/{id}")
        public String viewUserProfile(
                        @PathVariable Long id,
                        Model model) {
                User user = userRepository
                                .findById(id)
                                .orElseThrow();

                model.addAttribute("user", user);

                return "profile/user-profile";
        }

        @PostMapping("/profile")
        public String updateProfile(@RequestParam String fullName) {

                DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

                String currentEmail = principal.getEmail();

                User user = userRepository
                                .findUserByUsername(currentEmail)
                                .orElseThrow();

                user.setFullName(fullName);

                userRepository.save(user);

                return "redirect:/profile";
        }

        @PostMapping("profile/upload-picture")
        public String uploadProfilePicture(
                        @RequestParam("profilePicture") MultipartFile file) throws IOException {
                DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

                String email = principal.getEmail();

                User user = userRepository
                                .findUserByUsername(email)
                                .orElseThrow();

                if (!file.isEmpty()) {
                        String originalFilename = file.getOriginalFilename();

                        String extension = "";

                        if (originalFilename != null && originalFilename.contains(".")) {
                                extension = originalFilename.substring(
                                                originalFilename.lastIndexOf("."));
                        }

                        String filename = java.util.UUID.randomUUID() + extension;

                        java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads/profile-pictures");

                        java.nio.file.Files.createDirectories(uploadPath);

                        java.nio.file.Path filePath = uploadPath.resolve(filename);
                        java.nio.file.Files.copy(
                                        file.getInputStream(),
                                        filePath,
                                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                        user.setProfilePicture(filename);

                        userRepository.save(user);
                }

                return "redirect:/profile";
        }
}
