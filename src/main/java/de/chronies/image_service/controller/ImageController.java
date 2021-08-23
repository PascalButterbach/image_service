package de.chronies.image_service.controller;

import de.chronies.image_service.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageUploadService imageUploadService;


    @PostMapping({"", "/"})
    public ResponseEntity<List<String>> upload(@RequestParam("file") MultipartFile file) throws Exception {
       return ResponseEntity.ok(imageUploadService.initUpload(file));
    }

    @GetMapping({"", "/"})
    public String getAll(@RequestHeader("x-auth-user-id") Integer userId,
                         @RequestHeader("x-auth-user-email") String email) {
        return "Shouldnt be visible. Requesting UserID: " + userId + ", UserEmail: " + email;
    }

}
