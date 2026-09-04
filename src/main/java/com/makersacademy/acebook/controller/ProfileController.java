package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import com.makersacademy.acebook.repository.PostRepository;
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

        @Autowired
        PostRepository postRepository;

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
                User viewedUser = userRepository
                                .findById(id)
                                .orElseThrow();

                model.addAttribute("viewedUser", viewedUser);
                model.addAttribute("userPosts", postRepository.findByUserOrderByIdDesc(viewedUser));

                return "profile/user-profile";
        }

        @PostMapping("/profile")
        public String updateProfile(@RequestParam String fullName,
                                    @RequestParam String handle) {

                DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

                String currentEmail = principal.getEmail();

                User user = userRepository
                                .findUserByUsername(currentEmail)
                                .orElseThrow();

                handle = handle.toLowerCase();

                User existingHandle = userRepository
                        .findUserByHandle(handle)
                        .orElse(null);

                if (existingHandle != null && !existingHandle.getId().equals(user.getId())) {
                        return "redirect:/profile?handleTaken=true";
                }

                user.setFullName(fullName);
                user.setHandle(handle);

                userRepository.save(user);

                return "redirect:/profile?updated=true";
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
