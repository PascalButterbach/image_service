package de.chronies.image_service.controller;

import com.jcraft.jsch.ChannelSftp;
import de.chronies.image_service.config.FtpConfig;
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

    private final FtpConfig ftpConfig;

    @Value("${ftp.baseuri}")
    String baseUri;

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        Session<ChannelSftp.LsEntry> session = ftpConfig.sftpSessionFactory().getSession();

        String filename = RandomStringUtils.randomAlphanumeric(5) + "_" + file.getOriginalFilename();

        session.write(file.getInputStream(), filename);

        if(session.isOpen())
            session.close();

        return baseUri + filename;
    }

    @GetMapping({"", "/"})
    public String getAll(@RequestHeader("x-auth-user-id") Integer userId,
                         @RequestHeader("x-auth-user-email") String email) {
        return "Shouldnt be visible. Requesting UserID: " + userId + ", UserEmail: " + email;
    }

}
