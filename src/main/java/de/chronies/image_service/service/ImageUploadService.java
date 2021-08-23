package de.chronies.image_service.service;

import com.google.common.io.ByteSource;
import com.jcraft.jsch.ChannelSftp;
import de.chronies.image_service.config.FtpConfig;
import de.chronies.image_service.exceptions.ApiException;
import de.chronies.image_service.models.enums.FileSizeDir;
import de.chronies.image_service.models.enums.MimeType;
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
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final FtpConfig ftpConfig;
    private final ImageResizeService imageResizeService;

    @Value("${ftp.baseuri}")
    String BASE_URI;


    public List<String> initUpload(MultipartFile file) throws Exception {

        MimeType matchedMime = guessContentTypeFromStream(new ByteArrayInputStream(file.getBytes()));

        // resize
        Map<FileSizeDir, byte[]> finalFiles = new HashMap<>();
        finalFiles.put(FileSizeDir.ORIGINAL, file.getBytes());
        //        -small 128px * 128px
        //        -resized max width 1024px * ratio height
        //        - if original bigger then resized
        // create /thumb + /img + /orig
        finalFiles.putAll(imageResizeService.resize(file, matchedMime));

        // all same filename but different folder -> SAVE
        List<String> urls = uploadFile(finalFiles, matchedMime);

        // create Object for Database & persist

        // respond with Object / DTO / API RESPONSE

        return urls;
    }

    public List<String> uploadFile(Map<FileSizeDir, byte[]> resizedFiles, MimeType matchedMime) throws Exception {

        Session<ChannelSftp.LsEntry> session = ftpConfig.sftpSessionFactory().getSession();

        List<String> urls = new ArrayList<>();

        String randomFileName = RandomStringUtils.randomAlphanumeric(32);
        for (Map.Entry<FileSizeDir, byte[]> entry : resizedFiles.entrySet()) {
            String filename = entry.getKey().getDirectory() + randomFileName + matchedMime.getFileEnding();

            urls.add(BASE_URI + filename);

            InputStream inputStream = ByteSource.wrap(entry.getValue()).openStream();
            session.write(inputStream, filename);
        }

        if (session.isOpen())
            session.close();

        return urls;
    }

    public boolean removeFile(String[] fileNames) throws IOException {
        Session<ChannelSftp.LsEntry> session = ftpConfig.sftpSessionFactory().getSession();

        for (String fileName : fileNames) {
            session.remove(fileName);
        }

        if (session.isOpen())
            session.close();


        return true;
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

       /* for (FileType fileType : fileTypeComponent.getFileTypes()) {
            if (fileType.match(signature.toString()))
                return Optional.of(fileType);
        }*/

        throw new ApiException("bla", HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
