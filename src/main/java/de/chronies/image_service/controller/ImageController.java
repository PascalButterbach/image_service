package de.chronies.image_service.controller;

import de.chronies.image_service.model.Image;
import de.chronies.image_service.service.ImageService;
import de.chronies.image_service.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {

    private final ImageUploadService imageUploadService;
    private final ImageService imageService;


    @PostMapping("/")
    public ResponseEntity<List<String>> upload(@RequestHeader(name = "x-auth-user-id", defaultValue = "-1") Integer userId,
                                               @RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(imageUploadService.initUpload(userId, file));
    }

    @DeleteMapping("/{image_id}")
    public ResponseEntity<Boolean> removeUser(@PathVariable int image_id) throws IOException {
        return ResponseEntity.ok(imageService.delete(image_id));
    }

    @GetMapping("/")
    public ResponseEntity<List<Image>> getAll(@RequestHeader(name = "x-auth-user-id", defaultValue = "-1") Integer userId,
                                              @RequestHeader(name = "x-auth-user-email", defaultValue = "-1") String email) {
        return ResponseEntity.ok(imageService.getAll());
    }

    @GetMapping("/{image_id}")
    public ResponseEntity<Image> get(@PathVariable int image_id){
        return ResponseEntity.ok(imageService.findImageById(image_id));        
    }

}
