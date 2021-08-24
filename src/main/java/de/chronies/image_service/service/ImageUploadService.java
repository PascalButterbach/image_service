package de.chronies.image_service.service;

import com.google.common.io.ByteSource;
import com.jcraft.jsch.ChannelSftp;
import de.chronies.image_service.config.FtpConfig;
import de.chronies.image_service.exceptions.ApiException;
import de.chronies.image_service.model.Image;
import de.chronies.image_service.model.enums.FileSizeDir;
import de.chronies.image_service.model.enums.MimeType;
import de.chronies.image_service.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final FtpConfig ftpConfig;
    private final ImageResizeService imageResizeService;
    private final ImageRepository imageRepository;

    @Value("${ftp.baseuri}")
    String BASE_URI;


    public List<String> initUpload(Integer userId, MultipartFile file) throws Exception {

        MimeType matchedMime = guessContentTypeFromStream(new ByteArrayInputStream(file.getBytes()));

        Map<FileSizeDir, byte[]> finalFiles = new HashMap<>();
        finalFiles.put(FileSizeDir.ORIGINAL, file.getBytes());

        // resize
        finalFiles.putAll(imageResizeService.resize(file, matchedMime));

        // upload
        Map<String, String> urls = uploadFile(finalFiles, matchedMime);

        // create Object for Database & persist
        Image img = Image.builder()
                .user_id(userId)
                .path(urls.get("resized") == null ? urls.get("original") : urls.get("resized"))
                .path_thumbnail(urls.get("thumbnail"))
                .path_original(urls.get("resized") == null ? null : urls.get("original"))
                .build();

        if (imageRepository.create(img))
            return new ArrayList<>(urls.values());
        else
            throw new ApiException("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Map<String, String> uploadFile(Map<FileSizeDir, byte[]> resizedFiles, MimeType matchedMime) throws Exception {

        Session<ChannelSftp.LsEntry> session = ftpConfig.sftpSessionFactory().getSession();

        Map<String, String> urls = new HashMap<>();

        String randomFileName = RandomStringUtils.randomAlphanumeric(32);
        for (Map.Entry<FileSizeDir, byte[]> entry : resizedFiles.entrySet()) {
            String filename = entry.getKey().getDirectory() + randomFileName + matchedMime.getFileEnding();

            urls.put(entry.getKey().getType(), BASE_URI + filename);

            InputStream inputStream = ByteSource.wrap(entry.getValue()).openStream();
            session.write(inputStream, filename);
        }

        if (session.isOpen())
            session.close();

        return urls;
    }


    // Todo Throw on failure (IO Error : Not Supported Error)
    private MimeType guessContentTypeFromStream(InputStream is) throws IOException {
        if (!is.markSupported())
            throw new ApiException("bla", HttpStatus.INTERNAL_SERVER_ERROR);

        is.mark(16);
        StringBuilder signature = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            signature.append(String.format("%02X ", is.read()));
        }
        is.reset();

        for (MimeType mime : MimeType.values()) {
            for (String aByte : mime.getLeadingBytes()) {
                if (FilenameUtils.wildcardMatch(signature.toString(), aByte + "*"))
                    return mime;
            }
        }

        throw new ApiException("bla", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
