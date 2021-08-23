package de.chronies.image_service.controller;

import com.jcraft.jsch.ChannelSftp;
import de.chronies.image_service.config.FtpConfig;
import de.chronies.image_service.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Instant;
import java.time.LocalTime;


@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
public class ImageController {

    private final UploadService uploadService;


    @PostMapping({"", "/"})
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
       return uploadService.uploadFile(file);
    }

    @GetMapping({"", "/"})
    public String getAll(@RequestHeader("x-auth-user-id") Integer userId,
                         @RequestHeader("x-auth-user-email") String email) {
        return "Shouldnt be visible. Requesting UserID: " + userId + ", UserEmail: " + email;
    }

}
