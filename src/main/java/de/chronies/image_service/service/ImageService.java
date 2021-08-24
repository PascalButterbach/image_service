package de.chronies.image_service.service;

import com.jcraft.jsch.ChannelSftp;
import de.chronies.image_service.config.FtpConfig;
import de.chronies.image_service.exceptions.ApiException;
import de.chronies.image_service.model.Image;
import de.chronies.image_service.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FtpConfig ftpConfig;

    @Value("${ftp.baseuri}")
    String BASE_URI;

    public Image findImageById(int image_id) {
        return imageRepository.get(image_id)
                .orElseThrow(() -> new ApiException("Image not found.", HttpStatus.NOT_FOUND));
    }


    public boolean delete(int image_id) throws IOException {
        Session<ChannelSftp.LsEntry> session = ftpConfig.sftpSessionFactory().getSession();

        Image image = findImageById(image_id);

        if (image.getPath_original() != null) {
            session.remove(image.getPath_original().replaceFirst(BASE_URI, ""));
        }

        session.remove(image.getPath().replaceFirst(BASE_URI, ""));
        session.remove(image.getPath_thumbnail().replaceFirst(BASE_URI, ""));

        if (session.isOpen())
            session.close();

        return imageRepository.delete(image_id);
    }

}
