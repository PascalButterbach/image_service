package de.chronies.image_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {

    @GetMapping({"", "/"})
    public String getAll(@RequestHeader("x-auth-user-id")Integer userId,
                         @RequestHeader("x-auth-user-email") String email) {
        return "Shouldnt be visible. Requesting UserID: " + userId + ", UserEmail: " + email;
    }

}
